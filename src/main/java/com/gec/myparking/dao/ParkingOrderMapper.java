package com.gec.myparking.dao;

import com.gec.myparking.domain.ParkingOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ParkingOrderMapper {

    String TABLE_NAME = "parkingorder";
    String SELECT_FIELDS =" id , begin_time , end_time,user_id, car_port_id,price,duration,status";

    int deleteByPrimaryKey(Integer id);

    int insert(ParkingOrder record);

    int insertSelective(ParkingOrder record);

    ParkingOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ParkingOrder record);

    int updateByPrimaryKey(ParkingOrder record);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,"ORDER BY begin_time DESC "})
    List<ParkingOrder> selectAllOrders();

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,"WHERE user_id = #{userId} ORDER BY begin_time DESC"})
    List<ParkingOrder>  selectAllOrdersByUserId(Integer userId);
}