package com.gec.myparking.service;

import com.gec.myparking.domain.ParkingPort;
import com.gec.myparking.util.MyparkingUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParkingPortServiceTest {

	@Autowired
	ParkingPortService parkingPortService;

	@Test
	public void getPortsByStatus() {
		for (ParkingPort port : parkingPortService.getPortsByStatus(MyparkingUtil.PORT_STATUS_EMPTY)) {
			System.out.println(port.getCarportName());
		}
	}

	@Test
	public void getPortNameByStatus() {
		for (String portName : parkingPortService.getPortNameArrayByStatus(MyparkingUtil.PORT_STATUS_BOOKING)) {
			System.out.println(portName);
		}

	}

	@Test
	public void testStringJoin(){
		String[] bookingPortNameList = parkingPortService.getPortNameArrayByStatus(MyparkingUtil.PORT_STATUS_BOOKING);
		System.out.println(StringUtils.join(Arrays.asList(bookingPortNameList), ','));


	}
}