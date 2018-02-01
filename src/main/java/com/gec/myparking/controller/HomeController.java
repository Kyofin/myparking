package com.gec.myparking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home()
    {
        return "index";
    }

    @RequestMapping("/loginPage")
    public String loginPage()
    {
        return "home/login";
    }

    //数据中心路由
    @RequestMapping("/main")
    public String mainData()
    {
        return "home/main";
    }

}
