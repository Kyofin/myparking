package com.gec.myparking;

import com.gec.myparking.dao.CarMapper;

import com.gec.myparking.dao.ParkingOrderMapper;
import com.gec.myparking.dao.ParkingPortMapper;
import com.gec.myparking.dao.UserMapper;
import com.gec.myparking.domain.Car;

import com.gec.myparking.domain.ParkingOrder;
import com.gec.myparking.domain.ParkingPort;
import com.gec.myparking.domain.User;
import com.gec.myparking.util.MyparkingUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Daotest {
	private static final Logger logger = LoggerFactory.getLogger(Daotest.class);


	@Autowired
	CarMapper carMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	ParkingOrderMapper orderMapper;

	@Autowired
	ParkingPortMapper parkingPortMapper;

	@Test
	public void initDatabase() {
		for (int i = 0; i < 20; i++) {
			User user = new User();
			user.setId(i+1);
			user.setUserName("user"+(i+1));
			user.setCreateTime(new Date());
			user.setEmail("1040080742@qq.com");
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
			user.setSalt(new Random().nextInt(10)+"");
			user.setPassword(MyparkingUtil.MD5("123456"+user.getSalt()));
			userMapper.insert(user);
			//userMapper.deleteByPrimaryKey(i+1);

			Car car = new Car();
			car.setId(i+1);
			car.setCarNumber(String.format("粤A%d%d%d",new Random().nextInt(10),new Random().nextInt(10),new Random().nextInt(10)));
			car.setCarUserId(i+1);
			carMapper.insert(car);
			//carMapper.deleteByPrimaryKey(i+1);

			if (i>10)
			{
				ParkingPort port = new ParkingPort();
				port.setCarportName("A"+i);
				port.setParkingUserId(i+1);
				port.setStatus(MyparkingUtil.PORT_STATUS_EMPTY);
				parkingPortMapper.insert(port);

			}


			ParkingOrder order = new ParkingOrder();
			order.setBeginTime(new Date());
			order.setCarPortId(1);
			order.setUserId(1);
			order.setDuration(111111111L);
			order.setEndTime(new Date());
			order.setPrice(1111.222);
			orderMapper.insert(order);



		}


	}

	@Test
	public void testUpdate()
	{
		User user = new User();
		user.setId(1);
		user.setUpdateTime(new Date());
		user.setUserName("peter");
		userMapper.updateByPrimaryKeySelective(user);
	}

	@Test
	public  void testTime()
	{
	}
	@Test
	public  void  testInsert()
	{
		/*parkingPortMapper.insert(new ParkingPort(null,"A11",MyparkingUtil.PORT_STATUS_USED,2,"A11"));
		parkingPortMapper.insert(new ParkingPort(null,"A12",MyparkingUtil.PORT_STATUS_EMPTY,null,"A12"));
		parkingPortMapper.insert(new ParkingPort(null,"A13",MyparkingUtil.PORT_STATUS_USED,1,"A13"));
		parkingPortMapper.insert(new ParkingPort(null,"A14",0,null,"A14"));
		parkingPortMapper.insert(new ParkingPort(null,"A15",0,null,"A15"));*/
		parkingPortMapper.insert(new ParkingPort(null,"A16",MyparkingUtil.PORT_STATUS_BOOKING,3,"A16"));
	}

}
