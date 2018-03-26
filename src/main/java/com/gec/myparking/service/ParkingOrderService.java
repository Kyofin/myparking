package com.gec.myparking.service;

import com.gec.myparking.dao.ParkingOrderMapper;
import com.gec.myparking.domain.HostHolder;
import com.gec.myparking.domain.ParkingOrder;
import com.gec.myparking.domain.ParkingPort;
import com.gec.myparking.domain.User;
import com.gec.myparking.dto.ParkingOrderDTO;
import com.gec.myparking.util.Constant;
import com.gec.myparking.util.MyparkingUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ParkingOrderService {
    @Autowired
    private ParkingOrderMapper parkingOrderMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ParkingPortService portService;

    @Autowired
    private HostHolder hostHolder;


    public PageInfo<ParkingOrder> getOrders(Integer page, Integer limit) {
        List<ParkingOrder> parkingOrders= null;
        if (page!= null &&  limit!=null)
        {
            PageHelper.startPage(page,limit);
            parkingOrders =parkingOrderMapper.selectAllOrders();
        }else
        {
            PageHelper.startPage(1,10);
            parkingOrders =parkingOrderMapper.selectAllOrders();
        }
        //包装内容
        PageInfo<ParkingOrder> pageInfo = new PageInfo<>(parkingOrders);
        return  pageInfo;
    }

	public List<ParkingOrderDTO> getOrdersByUserId(Integer userId) {
        List<ParkingOrder> orders = parkingOrderMapper.selectAllOrdersByUserId(userId);
        List<ParkingOrderDTO> orderDTOS =new ArrayList<>();
        for (ParkingOrder order : orders) {
            User user = userService.getUserById(order.getUserId());
            ParkingPort port = portService.getPortById(order.getCarPortId());
            ParkingOrderDTO dto = new ParkingOrderDTO(order.getId(),order.getBeginTime(),order.getEndTime(),user,port,order.getPrice(),order.getDuration(),order.getStatus());
            orderDTOS.add(dto);
        }

        return orderDTOS;
    }

    public void insertOrder(ParkingOrder order){
        parkingOrderMapper.insert(order);
    }


    /**
     * 创建新订单对象
     * @param portId
     * @return
     */
    public ParkingOrder creatNewOrder(Integer portId ,Integer userId) {
        ParkingOrder order = new ParkingOrder();
        order.setUserId(userId);
        order.setCarPortId(portId);
        order.setBeginTime(new Date());
        order.setStatus(Constant.orderStatus.ORDER_STATUS_NOPAY);
        return order;
    }



    public ParkingOrder getOrdersByOrderId(Integer orderId) {
        if (orderId!=null){
            ParkingOrder order = parkingOrderMapper.selectByPrimaryKey(orderId);
            return order;
        }
        return null;
    }


    /**
     * todo 计费只取秒计算，为了测试数据比较直观
     * @param order
     */
    @Transactional
    public void payForOrder(ParkingOrder order) {
        if (order == null){
            return;
        }

        //计算时长
        Date beginDate = order.getBeginTime();
        Date endDate = new Date();
        long datePoorHour = MyparkingUtil.getDatePoor(endDate, beginDate);
        order.setEndTime(new Date());
        order.setDuration(datePoorHour);
        //计算价格
        order.setPrice(Double.valueOf(datePoorHour*1));

        //更改订单状态（变为已支付）
        order.setStatus(Constant.orderStatus.ORDER_STATUS_PAYED);

        //更改车位状态（变为空）todo 定时十五分钟后触发
        ParkingPort port = portService.getPortById(order.getCarPortId());
        port.setStatus(MyparkingUtil.PORT_STATUS_EMPTY);
        port.setParkingUserId(null);

        //持久化
        parkingOrderMapper.updateByPrimaryKeySelective(order);
        portService.updatePort(port);

    }

	public List<ParkingOrder> getOrdersByUserIdAndStatus(Integer id, Integer orderStatus) {
        return parkingOrderMapper.selectAllOrdersByUserIdAndStatus(id,orderStatus);

	}
}
