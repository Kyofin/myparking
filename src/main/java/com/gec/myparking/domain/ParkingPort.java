package com.gec.myparking.domain;

public class ParkingPort {
    private Integer id;

    private String carportName;

    private Integer status;

    private Integer parkingUserId;

    private String position;

    public ParkingPort(Integer id, String carportName, Integer status, Integer parkingUserId, String position) {
        this.id = id;
        this.carportName = carportName;
        this.status = status;
        this.parkingUserId = parkingUserId;
        this.position = position;
    }

    public ParkingPort() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarportName() {
        return carportName;
    }

    public void setCarportName(String carportName) {
        this.carportName = carportName == null ? null : carportName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getParkingUserId() {
        return parkingUserId;
    }

    public void setParkingUserId(Integer parkingUserId) {
        this.parkingUserId = parkingUserId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }
}