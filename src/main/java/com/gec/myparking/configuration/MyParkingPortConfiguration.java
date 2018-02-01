package com.gec.myparking.configuration;


import com.gec.myparking.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class MyParkingPortConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;



    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //验证用户ticket拦截器，成功则保留登录用户
        registry.addInterceptor(passportInterceptor);
        super.addInterceptors(registry);
    }
}
