package com.gec.myparking.controller;

import com.gec.myparking.domain.ParkingOrder;
import com.gec.myparking.domain.ParkingPort;
import com.gec.myparking.service.ParkingOrderService;
import com.gec.myparking.service.ParkingPortService;
import com.gec.myparking.service.UserService;
import com.gec.myparking.util.Constant;
import com.gec.myparking.util.MyparkingUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class ParkingOrderController {
	@Autowired
	private ParkingOrderService parkingOrderService;

	@Autowired
	private ParkingPortService parkingPortService;

	@Autowired
	private UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParkingOrderController.class);

	@RequestMapping("/orderPage")
	public String portPage() {
		return "order/orderList";
	}

	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	@ResponseBody
	public String getParkingPorts(@RequestParam(value = "page", required = false) Integer page,
								  @RequestParam(value = "limit", required = false) Integer limit) {
		try {
			PageInfo<ParkingOrder> parkingOrderPageInfo = parkingOrderService.getOrders(page, limit);
			List<Map> dataList = new ArrayList<>();
			Map<String, Object> map = new HashMap<>();
			map.put("count", parkingOrderPageInfo.getTotal());
			//组装data的展示数据
			for (ParkingOrder order : parkingOrderPageInfo.getList()) {
				Map dataMap = new HashMap();
				dataMap.put("id", order.getId());
				//获取车位的名字
				dataMap.put("carPortName", parkingPortService.getPortById(order.getCarPortId()).getCarportName());
				dataMap.put("orderUser", userService.getUserById(order.getUserId()).getUserName());
				dataMap.put("beginTime", order.getBeginTime());
				dataMap.put("endTime", order.getEndTime());
				dataMap.put("price", order.getPrice());
				dataMap.put("duration", order.getDuration());
				dataMap.put("status", order.getStatus());
				dataList.add(dataMap);
			}
			map.put("data", dataList);
			return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_SUCCESS, map, "获取订单列表成功");

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_FAIL, "发生异常，获取订单列表失败");
		}
	}




}
