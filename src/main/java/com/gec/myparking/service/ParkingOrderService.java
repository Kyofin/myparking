package com.gec.myparking.service;

import com.gec.myparking.dao.ParkingOrderMapper;
import com.gec.myparking.domain.ParkingOrder;
import com.gec.myparking.domain.ParkingPort;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingOrderService {
    @Autowired
    private ParkingOrderMapper parkingOrderMapper;

    public PageInfo<ParkingOrder> getOrders(Integer page, Integer limit) {
        List<ParkingOrder> parkingOrders= null;
        if (page!= null &&  limit!=null)
        {
            PageHelper.startPage(page,limit);
            parkingOrders =parkingOrderMapper.selectAllOrders();
        }else
        {
            PageHelper.startPage(1,10);
            parkingOrders =parkingOrderMapper.selectAllOrders();
        }
        //包装内容
        PageInfo<ParkingOrder> pageInfo = new PageInfo<>(parkingOrders);
        return  pageInfo;
    }
}
