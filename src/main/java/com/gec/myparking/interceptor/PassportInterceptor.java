package com.gec.myparking.interceptor;

import com.gec.myparking.dao.LoginTicketMapper;
import com.gec.myparking.dao.UserMapper;
import com.gec.myparking.domain.HostHolder;
import com.gec.myparking.domain.LoginTicket;
import com.gec.myparking.domain.User;
import com.gec.myparking.util.MyparkingUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    HostHolder hostHolder;

    //执行controller前
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        //循环查找客户端是否存在ticket
       if (httpServletRequest.getCookies()!=null)
       {
           for(Cookie cookie : httpServletRequest.getCookies())
           {
               if (cookie.getName().equals("ticket"))
               {
                   ticket = cookie.getValue();
                   break;
               }
           }
       }

        //验证该ticket是否真实存在
        LoginTicket loginTicket= null;
        if (ticket!=null )
        {
            loginTicket = loginTicketMapper.selectTicket(ticket);
        }

        //验证该ticket是否有效
        if(loginTicket==null || loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!= MyparkingUtil.LOGINTICKET_STATUS_USEFUL)
        {
            //其中一个条件满足则直接结束函数，不保存用户状态
            return  true;
        }

        //ticket通过验证，为有效用户，保存登录用户状态
        User loginUser = userMapper.selectByPrimaryKey(loginTicket.getUserId());
        hostHolder.setUser(loginUser);
        return true;
    }

    //渲染模板前
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null)
        {
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
