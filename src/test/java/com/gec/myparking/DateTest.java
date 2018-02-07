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

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DateTest {
	private static final Logger logger = LoggerFactory.getLogger(DateTest.class);

	@Test
	public  void  testJava8Date()
	{
		LocalDate today = LocalDate.now();
		System.err.println("今天是："+today);
		System.err.println("今天是："+new Date());

		int year = today.getYear();
		int month = today.getMonthValue();
		int day = today.getDayOfMonth();
		System.err.println(year+"年"+month+"月"+day+"日");
		System.err.println(today.getDayOfWeek());
		System.err.println(today.getDayOfYear());

		//创建指定的日期
		LocalDate dateOfBirth = LocalDate.of(2018,2,6);
		System.err.println(dateOfBirth);
		if (today.equals(dateOfBirth))
			System.err.println("是同一天");


		LocalDate dateOfMyBirth = LocalDate.of(2010, 01, 14);
		LocalDate TwoMonthLaterOfdateOfMyBirth = dateOfMyBirth.plus(2, ChronoUnit.MONTHS);
        System.err.println("两个月后："+TwoMonthLaterOfdateOfMyBirth);
		MonthDay birthday = MonthDay.of(dateOfMyBirth.getMonth(), dateOfMyBirth.getDayOfMonth());
		MonthDay currentMonthDay = MonthDay.from(today);

		if(currentMonthDay.equals(birthday)){
			System.out.println("Many Many happy returns of the day !!");
		}else{
			System.out.println("Sorry, today is not your birthday");
		}

	}



}
