package com.gec.myparking.AStart;

import java.util.Collections;
import java.util.List;

public class TestSVGMap {
	public static void main(String[] args) {
		int[][] mapArray = initSVGMapInfo();
		//起始点
		Node beginNode = new Node(35, 45);
		//终止点
		Node endNode = new Node(40, 25);
		//初始化地图信息
		MapInfo info=new MapInfo(mapArray,mapArray[0].length, mapArray.length,beginNode, endNode);
		//A*寻路开始
		new AStar().start(info);
		printMap(mapArray);
		//获取结果集合
		List<Coord> resultCoordList = AStar.getResultCoordList();
		//调整顺序
		Collections.reverse(resultCoordList);
		for (Coord coord : resultCoordList) {
			System.out.println(coord.x+":"+coord.y);
		}
		//获得svgpath
		getSVGPath(resultCoordList);
	}

	/**
	 * 初始化svg地图的信息
	 * 用0和1组成的二维数组表示
	 * @return
	 */
	public static int[][]  initSVGMapInfo()
	{
		//初始化map数组
		int[][] mapArray = new int[50][50];
		//标志障碍物
		for (int i = 0; i < mapArray.length; i++) {
			for (int k = 0; k < mapArray[i].length; k++) {
				//第一个区域 除了车位的点，其他都标志成障碍
				if (i<=10) {
					if(i==10 && k%10==5)
						continue;
					mapArray[i][k] = AStar.BAR;
				}
				//第二个区域 除了车位的点，其他都标志成障碍
				if (i>=20 && i<=40){
					if(i==20 && k%10==5 && k<30)
						continue;
					if(i==40 && k%10==5 && k<30)
						continue;
					if (k>30 && k<40)
						continue;
					if (k==40 && i%10==5)
						continue;
					mapArray[i][k] = AStar.BAR;
				}


			}
		}


		return mapArray;
	}

	/**
	 * 按照SVG格式，拼接svgpath，得到寻路路径结果
	 *
	 *  <path d="M60,50 L100,50 L100,100 " style="stroke: #006666; fill:none;"/>
	 *
	 * @param resultCoordList
	 */
	public static void getSVGPath(List<Coord> resultCoordList )
	{
		Coord startCoord = resultCoordList.get(0);
		StringBuilder pathStrb = new StringBuilder();

		pathStrb.append("<path d=\"M"+startCoord.x+","+startCoord.y+" ");

		for (int i = 1; i < resultCoordList.size(); i++) {
			Coord coord = resultCoordList.get(i);
			pathStrb.append("L"+coord.x+","+coord.y+" ");
		}
		pathStrb.append(" \" style=\"stroke: #006666; fill:none;\"/>");
		System.out.println(pathStrb.toString());
	}


	/**
	 * 打印地图
	 */
	public static void printMap(int[][] maps)
	{
		for (int i = 0; i < maps.length; i++)
		{
			for (int j = 0; j < maps[i].length; j++)
			{
				System.out.print(maps[i][j] + " ");
			}
			System.out.println();
		}
	}
}
