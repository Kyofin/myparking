package com.gec.myparking.domain;

public class Car {
    private Integer id;

    private String carNumber;

    private Integer carUserId;

    public Car(Integer id, String carNumber, Integer carUserId) {
        this.id = id;
        this.carNumber = carNumber;
        this.carUserId = carUserId;
    }

    public Car() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber == null ? null : carNumber.trim();
    }

    public Integer getCarUserId() {
        return carUserId;
    }

    public void setCarUserId(Integer carUserId) {
        this.carUserId = carUserId;
    }
}