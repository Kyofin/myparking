package com.gec.myparking.dao;

import com.gec.myparking.domain.Car;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CarMapper {
    String TABLE_NAME = "car";
    String SELECT_FIELDS =" id , car_number , car_user_id";

    int deleteByPrimaryKey(Integer id);

    int insert(Car record);

    int insertSelective(Car record);

    Car selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Car record);

    int updateByPrimaryKey(Car record);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME})
    List<Car> selectAllCars();
}