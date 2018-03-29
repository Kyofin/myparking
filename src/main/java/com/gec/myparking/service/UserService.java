package com.gec.myparking.service;

import com.gec.myparking.dao.LoginTicketMapper;
import com.gec.myparking.dao.UserMapper;
import com.gec.myparking.domain.Car;
import com.gec.myparking.domain.LoginTicket;
import com.gec.myparking.domain.User;
import com.gec.myparking.util.Constant;
import com.gec.myparking.util.MyparkingUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    LoginTicketService loginTicketService;



    /**
     * 登录服务
     * @param username
     * @param password
     * @return
     */
    public Map<String,Object> doLogin(String username, String password) {
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isEmpty(username))
        {
            map.put("error","用户名不能为空");
            return  map;//直接中断验证，返回错误信息
        }

        if (StringUtils.isEmpty(password))
        {
            map.put("error","密码不能为空");
            return  map;//直接中断验证，返回错误信息
        }

        User outUser = userMapper.selectByUserName(username);

        if (outUser==null)
        {
            map.put("error","该账号未注册");
            return  map;//直接中断验证，返回错误信息
        }

        if (!MyparkingUtil.MD5(password+outUser.getSalt()).equals(outUser.getPassword()))
        {
            map.put("error","用户或密码错误");
            return map ;  //直接中断验证，返回错误信息
        }


        //登录校验通过，下发ticket
        Integer userId = outUser.getId();
        LoginTicket loginTicket =loginTicketService.getLoginTicket(userId);
        System.out.println("登录成功下发ticket："+loginTicket.getTicket());
        map.put("ticket",loginTicket);

        return  map;

    }

    /**
     * 注册服务
     */
    public Map<String,Object> register(String username, String password ,String email ,String headUrl,String nickName) {
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isEmpty(username))
        {
            map.put("error","注册用户名不能为空");
            return  map;
        }

        if (StringUtils.isEmpty(password))
        {
            map.put("error","注册密码不能为空");
            return  map;
        }
        if (StringUtils.isEmpty(headUrl))
        {
            map.put("error","头像不能为空");
            return  map;
        }
        if (StringUtils.isEmpty(email))
        {
            map.put("error","email不能为空");
            return  map;
        }

        if (StringUtils.isEmpty(nickName))
        {
            map.put("error","昵称不能为空");
            return  map;
        }

        if (userMapper.selectByUserName(username)!=null)
        {
            map.put("error","该账号已注册");
            return  map;
        }

        //检验成功，加入到数据库
        User user = new User();
        user.setUserName(username);
        user.setSalt(UUID.randomUUID().toString().replace("-","").substring(0,5));
        user.setPassword(MyparkingUtil.MD5(password+user.getSalt()));
        user.setHeadUrl(headUrl);
        user.setCreateTime(new Date());
        user.setEmail(email);
        user.setNickName(nickName);
        user.setIsDeleted(Constant.IS_DELETED_FALSE);
        userMapper.insert(user);


        //登录校验通过，下发ticket
        Integer userId = userMapper.selectByUserName(username).getId();
        LoginTicket loginTicket = loginTicketService.getLoginTicket(userId);
        System.out.println("注册成功下发ticket："+loginTicket.getTicket());

        map.put("ticket",loginTicket);
        return map;
    }





    /**
     * 获得指定id用户
     * @param id
     * @return
     */
    public User getUserById(Integer id)
    {
        return userMapper.selectByPrimaryKey(id);
    }


    /**
     * 获取指定页的用户列表
     * @param page
     * @param limit
     * @return
     */
    public PageInfo<User> getUserList(Integer page, Integer limit) {
        List <User> userList= null;
        if (page!= null &&  limit!=null)
        {
            PageHelper.startPage(page,limit);
            userList =userMapper.selectAllUsers();
        }else
        {
            PageHelper.startPage(1,10);
            userList =userMapper.selectAllUsers();
        }
        //包装内容
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        return  pageInfo;
    }

    public void deleteUser(Integer id) {
        if (id !=null && userMapper.selectByPrimaryKey(id)!=null){
            User user = new User();
            user.setId(id);
            user.setUpdateTime(new Date());
            user.setIsDeleted(Constant.IS_DELETED_TRUE);
            userMapper.updateByPrimaryKeySelective(user);
        }
        else
            throw  new NullPointerException();
    }

    public Map addUser(String userName, String password, String email ,String headUrl ,String nickName) {
        Map<String ,Object> map = new HashMap<>();
        if (StringUtils.isEmpty(userName))
        {
            map.put("error","用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(nickName))
        {
            map.put("error","昵称不能为空");
            return map;
        }

        if (StringUtils.isEmpty(headUrl))
        {
            map.put("error","用户头像不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password))
        {
            map.put("error","密码不能为空");
            return map;
        }
        if (StringUtils.isEmpty(email))
        {
            map.put("error","邮箱不能为空");
            return map;
        }


        if (userMapper.selectByUserName(userName)!=null)
        {
            map.put("error","该用户名已经被注册了");
            return  map;
        }
        //持久化用户信息
        User user = new User();
        user.setUserName(userName);
        user.setSalt(UUID.randomUUID().toString().replace("-","").substring(0,5));
        user.setPassword(MyparkingUtil.MD5(password+user.getSalt()));
        user.setCreateTime(new Date());
        user.setHeadUrl(headUrl);
        user.setEmail(email);
        user.setNickName(nickName);
        user.setIsDeleted(Constant.IS_DELETED_FALSE);
        userMapper.insert(user);
        return  map;
    }




    public User getUserByUserName(String userName) {
        return userMapper.selectByUserName(userName);
    }

    public int addWxUser(String userName,String nickName, String headUrl) {
        User user = new User();
        user.setUserName(userName);
        user.setNickName(nickName);
        user.setHeadUrl(headUrl);
        user.setCreateTime(new Date());
        user.setIsDeleted(Constant.IS_DELETED_FALSE);
        return userMapper.insertSelective(user);
    }

    public void updateUser(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }
}
