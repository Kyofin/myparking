package com.gec.myparking.service;

import com.gec.myparking.AStart.Node;
import com.gec.myparking.AStart.SVGAdapter;
import com.gec.myparking.dao.ParkingPortMapper;
import com.gec.myparking.domain.HostHolder;
import com.gec.myparking.domain.ParkingOrder;
import com.gec.myparking.domain.ParkingPort;
import com.gec.myparking.util.Constant;
import com.gec.myparking.util.MyparkingUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkingPortService {
	@Autowired
	ParkingPortMapper parkingPortMapper;

	@Autowired
	HostHolder hostHolder;

	@Autowired
	SVGAdapter svgAdapter;

	@Autowired
	ParkingOrderService orderService;

	@Autowired
	WebSocket webSocket;


	/**
	 * 获取车位列表
	 *
	 * @param page
	 * @param limit
	 * @return
	 */
	public PageInfo<ParkingPort> getPorts(Integer page, Integer limit) {
		List<ParkingPort> parkingPortList = null;
		if (page != null && limit != null) {
			PageHelper.startPage(page, limit);
			parkingPortList = parkingPortMapper.selectAllPorts();
		} else {
			PageHelper.startPage(1, 10);
			parkingPortList = parkingPortMapper.selectAllPorts();
		}
		//包装内容
		PageInfo<ParkingPort> pageInfo = new PageInfo<>(parkingPortList);
		return pageInfo;
	}

	public String showPortInfoSVG() {
		//获得初始化图像
		return svgAdapter.getInitSVG();
	}

	public String showPath(String beginPortName, String endPortName) {
		//如果beginPortName缺省，则视为入口寻路
		Node beginNode = null;
		if (StringUtils.isEmpty(beginPortName)) {
			beginNode = svgAdapter.enterNode;
		}else {
			//获得beginPortName的坐标
			String[] strings = parkingPortMapper.selectPortPosition(beginPortName).split(",");
			beginNode = new Node(Integer.valueOf(strings[0]), Integer.valueOf(strings[1]));
		}



		//获得endPortName的坐标
		String[] strings = parkingPortMapper.selectPortPosition(endPortName).split(",");
		Node endNode = new Node(Integer.valueOf(strings[0]), Integer.valueOf(strings[1]));


		//获得初始化图像
		return svgAdapter.getResultSVG(beginNode, endNode);
	}

	public List<ParkingPort> getPortsByStatus(int status) {
		return parkingPortMapper.selectAllPortsByStatus(status);
	}

	public List<ParkingPort> getPortsByUserIdAndStatus(Integer userID,Integer status) {
		return parkingPortMapper.selectByUserIdAndStatus(userID,status);
	}

	public String[] getPortNameArrayByStatus(int status) {
		return parkingPortMapper.selectAllPortNameByStatus(status);

	}


	/**
	 * 预定车位
	 *
	 * @param portId
	 */
	public Map bookPort(Integer portId) {
		Map<String, Object> map = new HashMap();
		if (portId == null) {
			map.put("error", "车位id不能为空");
			return map;
		}

		ParkingPort outPort = parkingPortMapper.selectByPrimaryKey(portId);
		if (outPort == null) {
			map.put("error", "没有该车位");
			return map;
		}

		if (outPort.getStatus() == MyparkingUtil.PORT_STATUS_USED) {
			map.put("error", "车位已经被使用");
			return map;
		}

		if (outPort.getStatus() == MyparkingUtil.PORT_STATUS_BOOKING) {
			map.put("error", "车位已经被预定");
			return map;
		}
		//车位状态为空时更新车位状态被预定
		if (outPort.getStatus() == MyparkingUtil.PORT_STATUS_EMPTY) {
			outPort.setStatus(MyparkingUtil.PORT_STATUS_BOOKING);
			outPort.setParkingUserId(hostHolder.getUser().getId());
			parkingPortMapper.updateByPrimaryKeySelective(outPort);
		}

		return map;
	}

	public ParkingPort getPortById(Integer id) {
		return parkingPortMapper.selectByPrimaryKey(id);
	}


	@Transactional
	public Map usePort(Integer portId) throws Exception {
		Map<String, Object> map = new HashMap();
		if (portId == null) {
			map.put("error", "车位id不能为空");
			return map;
		}

		ParkingPort outPort = parkingPortMapper.selectByPrimaryKey(portId);
		if (outPort == null) {
			map.put("error", "没有该车位");
			return map;
		}

		if (outPort.getStatus() == MyparkingUtil.PORT_STATUS_USED) {
			map.put("error", "车位已经被使用");
			return map;
		}

		if (outPort.getStatus() == MyparkingUtil.PORT_STATUS_BOOKING) {
			//判断是否为自己预定的 (未登录会抛出空指针异常)
			if (outPort.getParkingUserId() != hostHolder.getUser().getId())
				map.put("error", "车位已经被他人预定");
			else {

				//更新车位
				outPort.setStatus(MyparkingUtil.PORT_STATUS_USED);
				outPort.setParkingUserId(hostHolder.getUser().getId());
				//生成订单
				ParkingOrder order = orderService.creatNewOrder(portId,hostHolder.getUser().getId());

				//持久化数据
				parkingPortMapper.updateByPrimaryKeySelective(outPort);
				orderService.insertOrder(order);

				//成功使用车位/停放车辆 ，反馈后台
				webSocket.sendMessage("车位："+outPort.getCarportName()+"已被使用");

			}
			return map;
		}
		//车位状态为空时更新车位状态被使用???
		/*if (outPort.getStatus() == MyparkingUtil.PORT_STATUS_EMPTY) {
			outPort.setStatus(MyparkingUtil.PORT_STATUS_USED);
			outPort.setParkingUserId(hostHolder.getUser().getId());
			//生成订单
			ParkingOrder order = orderService.creatNewOrder(portId, hostHolder.getUser().getId());
			//持久化数据
			parkingPortMapper.updateByPrimaryKeySelective(outPort);
			orderService.insertOrder(order);
		}*/

		map.put("error","你并未预订该车位！");
		return map;
	}



	public Map bookPortByPortName(String portName) {
		Map<String, Object> map = new HashMap();
		if (portName == null) {
			map.put("error", "车位名称不能为空");
			return map;
		}

		ParkingPort outPort = parkingPortMapper.selectByPortName(portName);
		if (outPort == null) {
			map.put("error", "没有该车位");
			return map;
		}

		if (outPort.getStatus() == MyparkingUtil.PORT_STATUS_USED) {
			map.put("error", "车位已经被使用");
			return map;
		}

		if (outPort.getStatus() == MyparkingUtil.PORT_STATUS_BOOKING) {
			map.put("error", "车位已经被预定");
			return map;
		}

		//
		Integer countByUserId = parkingPortMapper.countByParkingUserIdAndStatus(hostHolder.getUser().getId(),MyparkingUtil.PORT_STATUS_BOOKING);
		if (countByUserId >0) {
			map.put("error", "你已经预约车位，请不要重复预约");
			return map;
		}

		//车位状态为空时更新车位状态被预定
		if (outPort.getStatus() == MyparkingUtil.PORT_STATUS_EMPTY) {
			outPort.setStatus(MyparkingUtil.PORT_STATUS_BOOKING);
			outPort.setParkingUserId(hostHolder.getUser().getId());
			//持久化
			parkingPortMapper.updateByPrimaryKeySelective(outPort);
		}

		return map;
	}

	public Map cancelPort(Integer portId) {
		Map<String,Object> map = new HashMap<>();
		if (portId==null){
			map.put("error","车位id不能为空");
			return map;
		}

		//数据库中获取该车位
		ParkingPort outPort = parkingPortMapper.selectByPrimaryKey(portId);
		//更新车位状态为空
		outPort.setStatus(MyparkingUtil.PORT_STATUS_EMPTY);
		//更新车位用户为空
		outPort.setParkingUserId(null);
		//持久化数据
		parkingPortMapper.updateByPrimaryKey(outPort);

		return map;
	}


	public void updatePort(ParkingPort port) {
		parkingPortMapper.updateByPrimaryKey(port);
	}
}
