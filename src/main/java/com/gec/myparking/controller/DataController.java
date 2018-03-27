package com.gec.myparking.controller;

import com.alibaba.fastjson.JSONObject;
import com.gec.myparking.service.ParkingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("data")
@Controller
public class DataController {

	@Autowired
	ParkingOrderService orderService ;

	//todo 数据展示
	@RequestMapping("dayordercount")
	@ResponseBody
	public String getDayOrderCount(){
		List<Map> dayOrderCoutList = orderService.getDayOrderCount();
		List<String> category = new ArrayList<>();
		List<String> data = new ArrayList<>();
		for (Map map : dayOrderCoutList) {
			category.add(String.valueOf( map.get("mytime")));
			data.add(String.valueOf(map.get("myamount")) );
		}
		Map<String,Object> dataMap = new HashMap<>();
		dataMap.put("data",data.toArray());
		dataMap.put("category",category.toArray());
		return JSONObject.toJSONString(dataMap);


	}

}
