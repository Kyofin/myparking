package com.gec.myparking.util;

import com.gec.myparking.dao.ParkingPortMapper;
import com.gec.myparking.domain.ParkingPort;
import com.google.zxing.WriterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QRcodeUtilTest {

	@Autowired
	ParkingPortMapper portMapper;

	@Test
	public void writeQRcodeToFile() throws IOException, WriterException {
		List<ParkingPort> parkingPorts = portMapper.selectAllPorts();

		for (ParkingPort parkingPort : parkingPorts) {
			String url = Constant.CONTEXT_URL+"/parkingport/use/"+parkingPort.getId();
			QRcodeUtil.writeQRcodeToFile(url,parkingPort.getCarportName());
		}

	}
}