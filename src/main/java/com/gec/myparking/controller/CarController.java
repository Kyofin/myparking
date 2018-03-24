package com.gec.myparking.controller;

import com.alibaba.fastjson.JSONObject;
import com.gec.myparking.domain.Car;
import com.gec.myparking.service.CarService;
import com.gec.myparking.service.UserService;
import com.gec.myparking.util.Constant;
import com.gec.myparking.util.MyparkingUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/car")
public class CarController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    //访问车辆页面
    @RequestMapping("/carPage")
    public String CarPage()
    {
        return "car/carList";
    }
    //访问车辆页面
    @RequestMapping("/addCarPage")
    public String addCarPage(Model model)
    {

        return "car/carAdd";
    }

    @RequestMapping(value = "/cars",method = RequestMethod.GET)
    @ResponseBody
    public String getCars(@RequestParam(value = "page",required = false)Integer page ,
                          @RequestParam(value = "limit",required = false) Integer limit)
    {
        try {
            PageInfo<Car> carPageInfo= carService.getCars(page,limit);
             Map<String,Object> map = new HashMap<>();
            map.put("count",carPageInfo.getTotal());
            //组装data的展示数据
            List<Map> dataList = new ArrayList<>();
            for (Car car : carPageInfo.getList()) {
                Map dataMap = new HashMap();
                dataMap.put("id",car.getId());
                dataMap.put("carNumber",car.getCarNumber());
                dataMap.put("carUserName", userService.getUserById(car.getCarUserId()).getUserName());
                dataList.add(dataMap);
            }
            map.put("data",dataList);
            return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_SUCCESS,map,"获取车辆列表成功");
        }catch (Exception e)
        {
            e.printStackTrace();
           LOGGER.error(e.getMessage());
           return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_FAIL,"发生异常，获取车辆列表失败");
        }

    }

    @RequestMapping(value = "/cars",method = RequestMethod.POST)
    @ResponseBody
    public String addCar(@RequestParam String carNumber ,
                         @RequestParam Integer carUserId)
    {
        try {
            Map map = carService.addCar(carNumber,carUserId);
            if (map.isEmpty())
                return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_SUCCESS,"添加车辆成功");
            else
                return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_FAIL,map,"添加车辆失败");
        }catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_FAIL,"发生异常，添加车辆失败");
        }
    }


    @RequestMapping(value = "/cars",method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteCar(@RequestParam(value = "id")Integer id)
    {
        try {
            carService.deleteCar(id);
            return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_SUCCESS,"删除车辆成功");
        }catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(Constant.RESULT_STATUS_FAIL,"发生异常，删除车辆失败");
        }

    }
}
