package com.gec.myparking.controller;

import com.alibaba.fastjson.JSONObject;
import com.gec.myparking.domain.Car;
import com.gec.myparking.service.CarService;
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
@RequestMapping("/car")
public class CarController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarService carService;

    @RequestMapping(value = "/cars",method = RequestMethod.GET)
    @ResponseBody
    public String getCars(@RequestParam(value = "page",required = false)Integer page ,
                          @RequestParam(value = "limit",required = false) Integer limit)
    {
        try {
            PageInfo<Car> carPageInfo= carService.getCars(page,limit);
            Map<String,Object> map = new HashMap<>();
            map.put("count",carPageInfo.getTotal());
            map.put("data",carPageInfo.getList());
            return MyparkingUtil.getJsonString(0,map,"获取车辆列表成功");
        }catch (Exception e)
        {
           LOGGER.error(e.getMessage());
           return MyparkingUtil.getJsonString(1,"发生异常，获取车辆列表失败");
        }

    }

    @RequestMapping(value = "/cars",method = RequestMethod.POST)
    @ResponseBody
    public String addCar(@RequestParam String carNumber ,@RequestParam Integer carUserId)
    {
        try {
            Map map = carService.addCar(carNumber,carUserId);
            if (map.isEmpty())
                return MyparkingUtil.getJsonString(0,"添加车辆成功");
            else
                return MyparkingUtil.getJsonString(1,map,"添加车辆失败");
        }catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(1,"发生异常，添加车辆失败");
        }
    }


    @RequestMapping(value = "/cars",method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteCar(@RequestParam(value = "id")Integer id)
    {
        try {
            carService.deleteCar(id);
            return MyparkingUtil.getJsonString(0,"删除车辆成功");
        }catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(1,"发生异常，删除车辆失败");
        }

    }
}
