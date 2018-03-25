package com.gec.myparking.service;

import com.gec.myparking.dao.LoginTicketMapper;
import com.gec.myparking.domain.LoginTicket;
import com.gec.myparking.util.MyparkingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class LoginTicketService {
	@Autowired
	LoginTicketMapper loginTicketMapper;

	/**
	 * 持久化一个属于该用户的ticket，并返回
	 * @param userId
	 * @return
	 */
	public LoginTicket getLoginTicket(Integer userId) {
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(userId);
		loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));
		Date date = new Date();
		date.setTime(date.getTime()+1000*3600*24); //注册发放的，默认1天有效期
		loginTicket.setExpired(date);   //设置有效期
		loginTicket.setStatus(MyparkingUtil.LOGINTICKET_STATUS_USEFUL); //0为正常    1为失效
		//将新创的ticket加入数据库
		loginTicketMapper.insert(loginTicket);
		return loginTicket;
	}


	public void doLoginOut(String ticket) {
		loginTicketMapper.updateStatus(MyparkingUtil.LOGINTICKET_STATUS_NOTUSE,ticket);
	}
}
