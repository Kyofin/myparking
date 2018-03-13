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
	public void test() {
		int[][] mapArray = initMap2();
		//起始点
		Node beginNode = new Node(30, 45);
		//终止点
		Node endNode = new Node(70, 10);
		//初始化地图信息
		MapInfo info = new MapInfo(mapArray, mapArray[0].length, mapArray.length, beginNode, endNode);
		//A*寻路开始
		new AStar().start(info);
		svgAdapter.printMap(mapArray);
		//获取结果集合
		List<Coord> resultCoordList = AStar.getResultCoordList();
		//调整顺序
		Collections.reverse(resultCoordList);
		for (Coord coord : resultCoordList) {
			System.out.println(coord.x + ":" + coord.y);
		}
		//获得svgpath
		String path = svgAdapter.getSVGPath(resultCoordList);
		System.out.println(path);
		//获得起点圆
		String startCircle = svgAdapter.getStartPoint(beginNode.coord);
		System.out.println(startCircle);
	}
	@Test
	public void testResult(){

		//起始点
		Node beginNode = new Node(30, 45);
		//终止点
		Node endNode = new Node(70, 10);
		String resultSVG = svgAdapter.getResultSVG(beginNode, endNode);
		System.out.println(resultSVG);
	}

	public int[][]  initMap2() {
		//初始化map数组
		int[][] mapArray = new int[50][80];
		//标志障碍物
		for (int i = 0; i < mapArray.length; i++) {
			for (int k = 0; k < mapArray[i].length; k++) {
				mapArray[i][k] = AStar.BAR;
				if (i == 10)
					mapArray[i][k] = AStar.EMPTY;
				if (i == 20 && k <= 30)
					mapArray[i][k] = AStar.EMPTY;
				if (i == 30 && k >= 30)
					mapArray[i][k] = AStar.EMPTY;
				if (k == 30)
					mapArray[i][k] = AStar.EMPTY;
				if (i >= 20 && k == 10)
					mapArray[i][k] = AStar.EMPTY;
			}
		}
		return mapArray;
	}

	@Test
	public void printSVG() {
		int number = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 50; i++) {
			if (i % 4 == 0) {
				for (int j = 0; j < 50; j++) {
					if (j % 2 == 0) {
						getRect(number, i, j, sb);
					}
					number++;
				}
			}
		}

		System.out.println(sb.toString());
	}

	public void getRect(int number, int i, int j, StringBuilder sb) {
		String rect = "<rect id=\"svg_" + number + "\" height=\"2\" width=\"1\" y=\"" + i + "\" x=\"" + j + "\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0.2\" fill=\"#ffffff\" stroke=\"#000000\"/>";
		sb.append(rect);

	}

}
