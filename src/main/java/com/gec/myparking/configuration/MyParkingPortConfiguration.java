package com.gec.myparking.configuration;


import com.gec.myparking.interceptor.LoginRequiredInterceptor;
import com.gec.myparking.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class MyParkingPortConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;


    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //验证用户ticket拦截器，成功则保留登录用户
        registry.addInterceptor(passportInterceptor);
        //根据是否登录拦截页面
        registry.addInterceptor(loginRequiredInterceptor)
                .addPathPatterns("/order/**")
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/dologin")
                .addPathPatterns("/parkingport/**")
                .addPathPatterns("/car/**");


        super.addInterceptors(registry);
    }
}
