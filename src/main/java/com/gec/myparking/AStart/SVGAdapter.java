package com.gec.myparking.AStart;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SVGAdapter implements InitializingBean{

	private int[][] mapArray;

	//入口点
	public Node enterNode = new Node(30, 45);



	@Deprecated
	public  int[][]  initSVGMapInfo()
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
	 * 初始化svg地图的信息
	 * 用0和1组成的二维数组表示
	 * @return
	 */
	public int[][] initSVGMapInfo2()
	{
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


	/**
	 * 按照SVG格式，拼接svgpath，得到寻路路径结果
	 *
	 *  <path d="M60,50 L100,50 L100,100 " style="stroke: #006666; fill:none;"/>
	 *
	 * @param resultCoordList
	 */
	public static String getSVGPath(List<Coord> resultCoordList )
	{
		Coord startCoord = resultCoordList.get(0);
		StringBuilder pathStrb = new StringBuilder();

		pathStrb.append("<path d=\"M"+startCoord.x+","+startCoord.y+" ");

		for (int i = 1; i < resultCoordList.size(); i++) {
			Coord coord = resultCoordList.get(i);
			pathStrb.append("L"+coord.x+","+coord.y+" ");
		}
		pathStrb.append(" \" style=\"stroke: #FF0000; fill:none;\"/>");
		return pathStrb.toString();
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

	/**
	 * 标识起始点
	 * @throws Exception
	 */
	public String getStartPoint(Coord coord){
		return "<circle stroke=\"#ffffff\" id=\"svg_999\" r=\"1.29153\" cy=\""+coord.y+"\" cx=\""+coord.x+"\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" fill=\"#ff7f00\"/>\n";
	}


	public String getInitSVGStrNoHeadAndFoot(){

		return " <line x1=\"0\" stroke=\"#7f7f7f\" id=\"svg_3\" y2=\"10\" x2=\"80\" y1=\"10\" fill=\"none\"/>\n" +
				"  <line stroke=\"#7f7f7f\" id=\"svg_4\" y2=\"50\" x2=\"30\" y1=\"0\" x1=\"30\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" fill=\"none\"/>\n" +
				"  <line stroke=\"#7f7f7f\" id=\"svg_5\" y2=\"30\" x2=\"80\" y1=\"30\" x1=\"30\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" fill=\"none\"/>\n" +
				"  <line stroke=\"#7f7f7f\" id=\"svg_6\" y2=\"20.21252\" x2=\"30.07238\" y1=\"20.21252\" x1=\"0\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" fill=\"none\"/>\n" +
				"  <line stroke=\"#7f7f7f\" id=\"svg_7\" y2=\"50.11291\" x2=\"10.0498\" y1=\"20.47949\" x1=\"10.18329\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" fill=\"none\"/>\n" +
				"  <line id=\"svg_8\" y2=\"54.3844\" x2=\"28.87103\" y1=\"54.25091\" x1=\"28.87103\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"null\" stroke=\"#000000\" fill=\"none\"/>\n" +
				"  <rect stroke=\"#ffffff\" id=\"A0\" height=\"6.14026\" width=\"2.66968\" y=\"2.85962\" x=\"1.23987\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"A1\" height=\"6.14026\" width=\"2.66968\" y=\"2.85962\" x=\"5.39581\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#ffffff\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"A2\" height=\"6.14026\" width=\"2.66968\" y=\"2.85962\" x=\"9.80078\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#ffffff\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"A5\" height=\"6.14026\" width=\"2.66968\" y=\"2.85962\" x=\"25.94336\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#ffffff\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"B6\" height=\"6.14026\" width=\"2.66968\" y=\"2.72613\" x=\"31.53425\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#ffffff\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"B7\" height=\"6.14026\" width=\"2.66968\" y=\"2.72613\" x=\"35.30517\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#ffffff\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"C16\" height=\"6.14026\" width=\"2.66968\" y=\"11.06888\" x=\"31.30066\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"C17\" height=\"6.14026\" width=\"2.66968\" y=\"11.06888\" x=\"35.03821\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"B8\" height=\"6.14026\" width=\"2.66968\" y=\"2.72613\" x=\"38.75781\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#ffffff\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"B9\" height=\"6.14026\" width=\"2.66968\" y=\"2.72613\" x=\"42.91376\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#ffffff\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"B10\" height=\"6.14026\" width=\"2.66968\" y=\"2.72613\" x=\"47.31872\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#ffffff\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"C18\" height=\"6.14026\" width=\"2.66968\" y=\"11.06888\" x=\"38.49084\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"C19\" height=\"6.14026\" width=\"2.66968\" y=\"11.06888\" x=\"42.64679\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"C20\" height=\"6.14026\" width=\"2.66968\" y=\"11.06888\" x=\"47.05176\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"C21\" height=\"6.14026\" width=\"2.66968\" y=\"22.68198\" x=\"31.30066\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"C22\" height=\"6.14026\" width=\"2.66968\" y=\"22.68198\" x=\"34.77124\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"C23\" height=\"6.14026\" width=\"2.66968\" y=\"22.68198\" x=\"38.22388\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"C24\" height=\"6.14026\" width=\"2.66968\" y=\"22.68198\" x=\"42.37982\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"C25\" height=\"6.14026\" width=\"2.66968\" y=\"22.68198\" x=\"46.78479\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"F41\" height=\"6.14026\" width=\"2.66968\" y=\"31.22495\" x=\"31.30066\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"F42\" height=\"6.14026\" width=\"2.66968\" y=\"31.12484\" x=\"34.90472\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"F43\" height=\"6.14026\" width=\"2.66968\" y=\"31.22495\" x=\"38.35736\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"F44\" height=\"6.14026\" width=\"2.66968\" y=\"31.22495\" x=\"42.5133\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"F45\" height=\"6.14026\" width=\"2.66968\" y=\"31.22495\" x=\"46.91827\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\"/>\n" +
				"  <rect transform=\"rotate(90 5.262330055236814,24.283781051635746) \" stroke=\"#0000ff\" id=\"D31\" height=\"6.14026\" width=\"2.66968\" y=\"21.21365\" x=\"3.92749\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" fill=\"#7f7f00\"/>\n" +
				"  <rect transform=\"rotate(90 5.262330055236814,28.15481758117676) \" id=\"D32\" stroke=\"#0000ff\" height=\"6.14026\" width=\"2.66968\" y=\"25.08469\" x=\"3.92749\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" fill=\"#7f7f00\"/>\n" +
				"  <rect transform=\"rotate(90 5.262330055236814,31.892370223999027) \" id=\"D33\" stroke=\"#0000ff\" height=\"6.14026\" width=\"2.66968\" y=\"28.82224\" x=\"3.92749\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" fill=\"#7f7f00\"/>\n" +
				"  <rect transform=\"rotate(90 5.26233005523681,42.23736953735352) \" id=\"D34\" stroke=\"#0000ff\" height=\"6.14026\" width=\"2.66968\" y=\"39.16724\" x=\"3.92749\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"D35\" transform=\"rotate(90 5.26233005523681,46.10840225219727) \" stroke=\"#0000ff\" height=\"6.14026\" width=\"2.66968\" y=\"43.03827\" x=\"3.92749\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"E36\" transform=\"rotate(90 14.606199264526365,24.25041007995606) \" stroke=\"#0000ff\" height=\"6.14026\" width=\"2.66968\" y=\"21.18028\" x=\"13.27136\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"E37\" transform=\"rotate(90 14.606199264526365,28.12145042419434) \" stroke=\"#0000ff\" height=\"6.14026\" width=\"2.66968\" y=\"25.05132\" x=\"13.27136\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"E38\" transform=\"rotate(90 14.606199264526365,31.758876800537113) \" stroke=\"#0000ff\" height=\"6.14026\" width=\"2.66968\" y=\"28.68875\" x=\"13.27136\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" fill=\"#7f7f00\"/>\n" +
				"  <rect transform=\"rotate(90 14.606199264526362,42.33747863769532) \" id=\"E39\" stroke=\"#0000ff\" height=\"6.14026\" width=\"2.66968\" y=\"39.26735\" x=\"13.27136\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"E40\" transform=\"rotate(90 14.606199264526362,45.941539764404304) \" stroke=\"#0000ff\" height=\"6.14026\" width=\"2.66968\" y=\"42.87141\" x=\"13.27136\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"A3\" height=\"6.14026\" width=\"2.66968\" y=\"2.85962\" x=\"14.27249\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#ffffff\" fill=\"#7f7f00\"/>\n" +
				"  <rect id=\"A4\" height=\"6.14026\" width=\"2.66968\" y=\"2.85962\" x=\"22.28153\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#ffffff\" fill=\"#7f7f00\"/>\n" +
				"  <rect height=\"6.14026\" width=\"2.66968\" y=\"22.68198\" x=\"58.95673\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\" id=\"C26\"/>\n" +
				"  <rect height=\"6.14026\" width=\"2.66968\" y=\"22.68198\" x=\"62.42731\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\" id=\"C27\"/>\n" +
				"  <rect height=\"6.14026\" width=\"2.66968\" y=\"22.68198\" x=\"65.87995\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\" id=\"C28\"/>\n" +
				"  <rect height=\"6.14026\" width=\"2.66968\" y=\"22.68198\" x=\"70.03589\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\" id=\"C29\"/>\n" +
				"  <rect height=\"6.14026\" width=\"2.66968\" y=\"22.68198\" x=\"74.44086\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\" id=\"C30\"/>\n" +
				"  <rect height=\"6.14026\" width=\"2.66968\" y=\"2.72613\" x=\"58.82324\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\" id=\"B11\"/>\n" +
				"  <rect height=\"6.14026\" width=\"2.66968\" y=\"2.72613\" x=\"62.56079\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\" id=\"B12\"/>\n" +
				"  <rect height=\"6.14026\" width=\"2.66968\" y=\"2.72613\" x=\"66.01342\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\" id=\"B13\"/>\n" +
				"  <rect height=\"6.14026\" width=\"2.66968\" y=\"2.72613\" x=\"70.16937\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\" id=\"B14\"/>\n" +
				"  <rect height=\"6.14026\" width=\"2.66968\" y=\"2.72613\" x=\"74.57434\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"0\" stroke=\"#000000\" fill=\"#7f7f00\" id=\"B15\"/>\n";

	}


	/**
	 * 获取初始化SVG图像
	 * @return
	 */
	public String getInitSVG(){
		//拼接头
		String result = "<svg width=\"800\" height=\"500\" viewBox=\"0 0 80 50\"  xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\">";
		result+=getInitSVGStrNoHeadAndFoot();
		result+="</svg>\n";
		return result;
	}

	/**
	 * 返回拼接的最终结果
	 */
	public String getResultSVG(Node beginNode,Node endNode ){
		//拼接头
		String result = "<svg width=\"800\" height=\"500\" viewBox=\"0 0 80 50\"  xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\">";
		//拼接原始图像
		result += getInitSVGStrNoHeadAndFoot();
		//初始化地图信息
		MapInfo info = new MapInfo(mapArray, mapArray[0].length, mapArray.length, beginNode, endNode);
		//A*寻路开始
		new AStar().start(info);
		printMap(mapArray);
		//获取结果集合
		List<Coord> resultCoordList = AStar.getResultCoordList();
		//调整顺序
		Collections.reverse(resultCoordList);
		for (Coord coord : resultCoordList) {
			System.out.println(coord.x + ":" + coord.y);
		}
		//获得svgpath
		String path = getSVGPath(resultCoordList);
		result+=path;

		//获得起点圆
		String startCircle = getStartPoint(beginNode.coord);
		result+=startCircle;

		//拼接尾部
		result+="</svg>\n";

		return result;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		mapArray = initSVGMapInfo2();
	}
}
