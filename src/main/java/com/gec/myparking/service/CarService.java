package com.gec.myparking.service;

import com.gec.myparking.dao.CarMapper;
import com.gec.myparking.dao.UserMapper;
import com.gec.myparking.domain.Car;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarService {

	@Autowired
	private CarMapper carMapper;

	@Autowired
	private UserMapper userMapper;

	public Map addCar(String carNumber, Integer carUserId) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isEmpty(carNumber)) {
			map.put("error", "车牌号码不能不为空");
			return map;
		}

		if (carUserId == null) {
			map.put("error", "车辆拥有者不能为空");
			return map;
		}

		if (userMapper.selectByPrimaryKey(carUserId) == null) {
			map.put("error", "没有该用户");
			return map;
		}
		//持久化车辆信息
		Car newCar = new Car(null, carNumber, carUserId);
		carMapper.insert(newCar);
		return map;
	}

	public PageInfo<Car> getCars(Integer page, Integer limit) {
		List<Car> carList = null;
		if (page != null && limit != null) {
			PageHelper.startPage(page, limit);
			carList = carMapper.selectAllCars();
		} else {
			PageHelper.startPage(1, 10);
			carList = carMapper.selectAllCars();
		}
		//包装内容
		PageInfo<Car> pageInfo = new PageInfo<>(carList);
		return pageInfo;
	}


	public List<Car> getCarsByUserId(Integer userId) {
		List<Car> carList = carMapper.selectAllCarsByUserId(userId);
		return carList;

	}

	public void deleteCar(Integer id) {
		if (id != null) {
			carMapper.deleteByPrimaryKey(id);
		} else
			throw new NullPointerException();
	}
}
