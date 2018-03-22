package com.gec.myparking.controller;

import com.gec.myparking.domain.Car;
import com.gec.myparking.domain.HostHolder;
import com.gec.myparking.domain.ParkingPort;
import com.gec.myparking.service.ParkingPortService;
import com.gec.myparking.service.UserService;
import com.gec.myparking.util.MyparkingUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/parkingport")
public class ParkingPortController {

    @Autowired
    ParkingPortService parkingPortService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingPortController.class);

    @RequestMapping("/portPage")
    public  String portPage()
    {
        return "port/portList";
    }

    @RequestMapping(value = "/parkingports",method = RequestMethod.GET)
    @ResponseBody
    public String getParkingPorts(@RequestParam(value = "page",required = false) Integer page,
                                  @RequestParam(value = "limit",required = false) Integer limit)
    {
        try {
            PageInfo<ParkingPort> parkingPortPageInfo= parkingPortService.getPorts(page,limit);
            Map<String,Object> map = new HashMap<>();
            map.put("count",parkingPortPageInfo.getTotal());
            //组装data的展示数据
            List<Map> dataList = new ArrayList<>();
            for (ParkingPort  port: parkingPortPageInfo.getList()) {
                Map dataMap = new HashMap();
                dataMap.put("id",port.getId());
                dataMap.put("carportName",port.getCarportName());
                //判断车位是否有人预定或者使用
                Integer userId = port.getParkingUserId();
                if (userId!=null)
                {
                    if (userService.getUserById(userId)!=null)
                        dataMap.put("parkingUser",userService.getUserById(userId).getUserName());
                }
                else
                    dataMap.put("parkingUser","无");  //没人使用的情况返回前端无
                dataMap.put("status",port.getStatus());
                dataList.add(dataMap);
            }
            map.put("data",dataList);
            return MyparkingUtil.getJsonString(0,map,"获取车位列表成功");

        }catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(1,"发生异常，获取车位列表失败");
        }
    }

    @RequestMapping(value = "/book/{portId}",method = RequestMethod.GET)
    @ResponseBody
    public  String bookPort(@PathVariable("portId") Integer portId)
    {
        try {
            Map map =  parkingPortService.bookPort(portId);
            if (map.isEmpty())
            {
                return MyparkingUtil.getJsonString(0, "预定车位成功");
            }else
                return MyparkingUtil.getJsonString(1,map,"预定车位失败");
        }catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(1,"发生异常，预定车位失败");
        }
    }


    @RequestMapping(value = "/cancel",method = RequestMethod.POST)
    @ResponseBody
    public  String cancelBookPort(@RequestParam("portId") Integer portId)
    {
        try {
            Map map =  parkingPortService.cancelPort(portId);
            if (map.isEmpty())
            {
                return MyparkingUtil.getJsonString(0, "取消预定车位成功");
            }else
                return MyparkingUtil.getJsonString(1,map,"取消预定车位失败");
        }catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(1,"发生异常，取消预定车位失败");
        }
    }


    @RequestMapping(value = "/book/{portName}",method = RequestMethod.POST)
    @ResponseBody
    public  String bookPortByPortName(@PathVariable("portName") String portName)
    {
        try {
            Map map =  parkingPortService.bookPortByPortName(portName);
            if (map.isEmpty())
            {
                return MyparkingUtil.getJsonString(0, "预定车位成功");
            }else
                return MyparkingUtil.getJsonString(1,map,"预定车位失败");
        }catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(1,"发生异常，预定车位失败");
        }
    }


    /**
     * 扫二维码触发
     * @param portId
     * @return
     */
    @RequestMapping(value = "/use/{portId}")
    @ResponseBody
    public  String usePort(@PathVariable("portId") Integer portId)
    {
        try {
            //获取当前用户是否正在使用车位
            List<ParkingPort> ports = parkingPortService.getPortsByUserIdAndStatus(hostHolder.getUser().getId(), MyparkingUtil.PORT_STATUS_USED);

            //已经在使用车位
            if (ports!=null){
                //获取两个车位的信息
                ParkingPort beginPort = parkingPortService.getPortById(portId);
                ParkingPort endPort = ports.get(0);
                String beginPortName = beginPort.getCarportName();
                String endPortName = endPort.getCarportName();
                Map<String,Object> map = new HashMap<>();
                map.put("beginPortName",beginPortName);
                map.put("endPortName",endPortName);
                return MyparkingUtil.getJsonString(0,map,"反向寻车成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(1,"反向寻车失败");

        }


        //没有使用车位
        try {
            Map map =  parkingPortService.usePort(portId);
            if (map.isEmpty())
            {
                return MyparkingUtil.getJsonString(0, "使用车位成功");
            }else
                return MyparkingUtil.getJsonString(1,map,"使用车位失败");
        }catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return MyparkingUtil.getJsonString(1,"发生异常，使用车位失败");
        }
    }

}
