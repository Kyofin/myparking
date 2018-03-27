package com.gec.myparking.dao;

import com.gec.myparking.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    String TABLE_NAME = "user";
    String SELECT_FIELDS =" id , user_name , password, salt, email, create_time,update_time,head_url,nick_name,is_deleted";


    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加用户
     * @param record
     * @return
     */
    int insert(User record);


    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 通话用户名查询获得该用户
     * @param username
     * @return
     */
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where user_name = #{username} AND is_deleted = 0"})
    User selectByUserName(String username);

    /**
     * 获得用户表所有用户
     * @return
     */
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,"WHERE is_deleted = 0"})
    List<User> selectAllUsers();
}