package com.gec.myparking.controller;

import com.gec.myparking.domain.ParkingPort;
import com.gec.myparking.service.ParkingPortService;
import com.gec.myparking.util.MyparkingUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/parkingport")
public class ParkingPortController {

    @Autowired
    ParkingPortService parkingPortService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingPortController.class);

    @RequestMapping(value = "/parkingports",method = RequestMethod.GET)
    @ResponseBody
    public String getParkingPorts(@RequestParam(value = "page",required = false) Integer page,
                                  @RequestParam(value = "limit",required = false) Integer limit)
    {
        try {
            PageInfo<ParkingPort> parkingPortPageInfo= parkingPortService.getPorts(page,limit);
            Map<String,Object> map = new HashMap<>();
            map.put("count",parkingPortPageInfo.getTotal());
            map.put("data",parkingPortPageInfo.getList());
            return MyparkingUtil.getJsonString(0,map,"获取车位列表成功");

        }catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(1,"发生异常，获取车位列表失败");
        }
    }

    @RequestMapping(value = "/book/{portId}",method = RequestMethod.GET)
    @ResponseBody
    public  String bookPort(@PathVariable("portId") Integer portId)
    {
        try {
           Map map =  parkingPortService.bookPort(portId);
           if (map.isEmpty())
           {
               return MyparkingUtil.getJsonString(0, "预定车位成功");
           }else
               return MyparkingUtil.getJsonString(1,map,"预定车位失败");
        }catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(1,"发生异常，预定车位失败");
        }
    }


    @RequestMapping(value = "/use/{portId}",method = RequestMethod.GET)
    @ResponseBody
    public  String usePort(@PathVariable("portId") Integer portId)
    {
        try {
            Map map =  parkingPortService.usePort(portId);
            if (map.isEmpty())
            {
                return MyparkingUtil.getJsonString(0, "使用车位成功");
            }else
                return MyparkingUtil.getJsonString(1,map,"使用车位失败");
        }catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(1,"发生异常，使用车位失败");
        }
    }

}
