package com.gec.myparking.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ParkingOrderMapperTest {

	@Autowired
	ParkingOrderMapper orderMapper;

	@Test
	public void getDayOrderCount() {

	}
}