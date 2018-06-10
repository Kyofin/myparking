package com.gec.myparking.controller;

import com.gec.myparking.execption.ExceptionEnum;
import com.gec.myparking.execption.GirlExecption;
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

    /**
     * 全局异常处理测试路由
     * 如果没有填code参数，则不会走下面程序，而是直接抛出一个异常
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/myexecption")
    public String testExecption(int code) throws Exception {
        if (code == 0) {
            throw new GirlExecption(ExceptionEnum.AGE_EXCEPTION);
        } else if (code == 1) {
            throw new GirlExecption(ExceptionEnum.HEIGHT_EXCEPTION);
        }

        throw new Exception("默认异常信息");
    }


}
