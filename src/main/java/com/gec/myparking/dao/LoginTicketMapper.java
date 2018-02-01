package com.gec.myparking.dao;

import com.gec.myparking.domain.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LoginTicketMapper {
    String TABLE_NAME = "login_ticket";
    String INSERT_FIELDS = "user_id , ticket , expired  , status";
    String SELECT_FIELDS =" id , "+ INSERT_FIELDS;


    int deleteByPrimaryKey(Integer id);

    int insert(LoginTicket record);

    int insertSelective(LoginTicket record);

    LoginTicket selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoginTicket record);

    int updateByPrimaryKey(LoginTicket record);

    @Update({"update",TABLE_NAME,"set status = #{status} where ticket = #{ticket}"})
    void  updateStatus(@Param("status") int status, @Param("ticket") String ticket);


    @Select({"select  ",SELECT_FIELDS,"from",TABLE_NAME," where ticket = #{ticket}"})
    LoginTicket selectTicket(String ticket);
}