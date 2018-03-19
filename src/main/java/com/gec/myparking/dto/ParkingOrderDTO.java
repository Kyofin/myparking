package com.gec.myparking.dto;

import com.gec.myparking.domain.ParkingPort;
import com.gec.myparking.domain.User;

import java.util.Date;

public class ParkingOrderDTO {
	private Integer id;

	private Date beginTime;

	private Date endTime;

	private User user;

	private ParkingPort carPort;

	private Double price;

	private Long duration;

	private Integer status;

	public ParkingOrderDTO() {
	}

	public ParkingOrderDTO(Integer id, Date beginTime, Date endTime, User user, ParkingPort carPort, Double price, Long duration, Integer status) {
		this.id = id;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.user = user;
		this.carPort = carPort;
		this.price = price;
		this.duration = duration;
		this.status = status;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ParkingPort getCarPort() {
		return carPort;
	}

	public void setCarPort(ParkingPort carPort) {
		this.carPort = carPort;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
