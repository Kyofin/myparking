package com.gec.myparking;

import com.gec.myparking.AStart.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SVGtest {
	private static final Logger logger = LoggerFactory.getLogger(SVGtest.class);

	@Autowired
	private SVGAdapter svgAdapter;

	@Test
	public void test(){
		int[][] mapArray = svgAdapter.initSVGMapInfo();
		//起始点
		Node beginNode = new Node(35, 45);
		//终止点
		Node endNode = new Node(40, 25);
		//初始化地图信息
		MapInfo info=new MapInfo(mapArray,mapArray[0].length, mapArray.length,beginNode, endNode);
		//A*寻路开始
		new AStar().start(info);
		svgAdapter.printMap(mapArray);
		//获取结果集合
		List<Coord> resultCoordList = AStar.getResultCoordList();
		//调整顺序
		Collections.reverse(resultCoordList);
		for (Coord coord : resultCoordList) {
			System.out.println(coord.x+":"+coord.y);
		}
		//获得svgpath
		String path = svgAdapter.getSVGPath(resultCoordList);
		System.out.println(path);
	}

}
