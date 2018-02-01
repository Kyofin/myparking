package com.gec.myparking.service;

import com.gec.myparking.dao.LoginTicketMapper;
import com.gec.myparking.dao.UserMapper;
import com.gec.myparking.domain.Car;
import com.gec.myparking.domain.LoginTicket;
import com.gec.myparking.domain.User;
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
    LoginTicketMapper loginTicketMapper;

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
            map.put("error","用户密码错误");
            return map ;  //直接中断验证，返回错误信息
        }


        //登录校验通过，下发ticket
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(outUser.getId());
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));
        Date date = new Date();
        date.setTime(date.getTime()+1000*3600*24*10);  //10天有效期
        loginTicket.setExpired(date);   //设置有效期
        loginTicket.setStatus(MyparkingUtil.LOGINTICKET_STATUS_USEFUL); //0为正常    1为失效
        //将新创的ticket加入数据库
        loginTicketMapper.insert(loginTicket);


        System.out.println("登录成功下发ticket："+loginTicket.getTicket());

        map.put("ticket",loginTicket);

        return  map;

    }

    /**
     * 注册服务
     * @param username
     * @param password
     * @return
     */
    public Map<String,Object> register(String username, String password) {
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
        String headUrl = String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000));
        user.setHeadUrl(headUrl);
        user.setCreateTime(new Date());
        user.setEmail("1040080742@qq.com");
        userMapper.insert(user);

        //登录校验通过，下发ticket
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userMapper.selectByUserName(username).getId());
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));
        Date date = new Date();
        date.setTime(date.getTime()+1000*3600*24); //注册发放的，默认1天有效期
        loginTicket.setExpired(date);   //设置有效期
        loginTicket.setStatus(0); //0为正常    1为失效
        //将新创的ticket加入数据库
        loginTicketMapper.insert(loginTicket);
        System.out.println("注册成功下发ticket："+loginTicket.getTicket());

        map.put("ticket",loginTicket);
        return map;
    }

    public void doLoginOut(String ticket) {
        loginTicketMapper.updateStatus(MyparkingUtil.LOGINTICKET_STATUS_NOTUSE,ticket);
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
        if (id !=null)
            userMapper.deleteByPrimaryKey(id);
        else
            throw  new NullPointerException();
    }

    public Map addUser(String userName, String password, String email) {
        Map<String ,Object> map = new HashMap<>();
        if (StringUtils.isEmpty(userName))
        {
            map.put("error","用户名不能为空");
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
        String headUrl = String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000));
        user.setHeadUrl(headUrl);
        user.setEmail(email);
        userMapper.insert(user);
        return  map;
    }
}
