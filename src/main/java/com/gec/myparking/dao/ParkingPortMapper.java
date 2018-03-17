package com.gec.myparking.dao;

import com.gec.myparking.domain.ParkingPort;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ParkingPortMapper {

    String TABLE_NAME = "parkingport";
    String SELECT_FIELDS =" id , carport_name , status,parking_user_id, position";

    int deleteByPrimaryKey(Integer id);

    int insert(ParkingPort record);

    int insertSelective(ParkingPort record);

    ParkingPort selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ParkingPort record);

    int updateByPrimaryKey(ParkingPort record);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME})
    List<ParkingPort> selectAllPorts();

    @Select({"SELECT ",SELECT_FIELDS,"FROM ",TABLE_NAME," WHERE status = #{status}"})
    List<ParkingPort> selectAllPortsByStatus(int status);

    @Select({"SELECT carport_name","FROM ",TABLE_NAME," WHERE status = #{status}"})
    String[] selectAllPortNameByStatus(int status);

    @Select({"SELECT POSITION ","FROM ",TABLE_NAME," WHERE carport_name = #{carPortName}"})
    String selectPortPosition(String carPortName);

    @Select({"SELECT  ",SELECT_FIELDS,"FROM ",TABLE_NAME," WHERE carport_name = #{carPortName}"})
    ParkingPort selectByPortName(String portName);
}