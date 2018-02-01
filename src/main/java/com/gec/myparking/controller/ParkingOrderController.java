package com.gec.myparking.controller;

import com.gec.myparking.domain.ParkingOrder;
import com.gec.myparking.service.ParkingOrderService;
import com.gec.myparking.util.MyparkingUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class ParkingOrderController {
    @Autowired
    private ParkingOrderService parkingOrderService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingOrderController.class);

    @RequestMapping(value = "/orders",method = RequestMethod.GET)
    @ResponseBody
    public String getParkingPorts(@RequestParam(value = "page",required = false) Integer page,
                                  @RequestParam(value = "limit",required = false) Integer limit)
    {
        try {
            PageInfo<ParkingOrder> parkingOrderPageInfo= parkingOrderService.getOrders(page,limit);
            Map<String,Object> map = new HashMap<>();
            map.put("count",parkingOrderPageInfo.getTotal());
            map.put("data",parkingOrderPageInfo.getList());
            return MyparkingUtil.getJsonString(0,map,"获取订单列表成功");

        }catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(1,"发生异常，获取订单列表失败");
        }
    }

}
