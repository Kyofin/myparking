package com.gec.myparking.domain;

import java.util.Date;

public class ParkingOrder {
    private Integer id;

    private Date beginTime;

    private Date endTime;

    private Integer userId;

    private Integer carPortId;

    private Double price;

    private Long duration;

    public ParkingOrder(Integer id, Date beginTime, Date endTime, Integer userId, Integer carPortId, Double price, Long duration) {
        this.id = id;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.userId = userId;
        this.carPortId = carPortId;
        this.price = price;
        this.duration = duration;
    }

    public ParkingOrder() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCarPortId() {
        return carPortId;
    }

    public void setCarPortId(Integer carPortId) {
        this.carPortId = carPortId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}