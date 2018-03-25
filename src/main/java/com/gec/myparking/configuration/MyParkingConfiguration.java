package com.gec.myparking.configuration;


import com.gec.myparking.interceptor.AdminLoginRequiredInterceptor;
import com.gec.myparking.interceptor.PassportInterceptor;
import com.gec.myparking.interceptor.WxLoginRequiredInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class MyParkingConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;
    @Autowired
    AdminLoginRequiredInterceptor adminLoginRequiredInterceptor;
    @Autowired
    WxLoginRequiredInterceptor wxLoginRequiredInterceptor;


    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //验证用户ticket拦截器，成功则保留登录用户（所有访问）
        registry.addInterceptor(passportInterceptor);
        //根据是否登录拦截后台页面（部分访问）
        registry.addInterceptor(adminLoginRequiredInterceptor)
                .addPathPatterns("/")
                .addPathPatterns("/order/**")
                .addPathPatterns("/user/**")
                .addPathPatterns("/parkingport/**")
                .addPathPatterns("/car/**")
                .excludePathPatterns("/user/dologin");
        //根据是否登录拦截微信页面（部分访问）
        registry.addInterceptor(wxLoginRequiredInterceptor)
                .addPathPatterns("/portal/user/**")
                .excludePathPatterns("/portal/user/loginPage")
                .excludePathPatterns("/portal/user/auth")
                .excludePathPatterns("/portal/user/getUserInfo")
                .excludePathPatterns("/portal/user/uploadImage")
                .excludePathPatterns("/portal/user/register")
                .excludePathPatterns("/portal/user/registerPage");



        super.addInterceptors(registry);
    }
}
