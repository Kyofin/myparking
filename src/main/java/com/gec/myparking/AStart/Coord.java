package com.gec.myparking.AStart;
/**
 * 
 * ClassName: Coord
 * 
 * @Description: 坐标
 * @author peter
 */
public class Coord
{
	public int x;
	public int y;

	public Coord(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * 横坐标和纵坐标相同才是相等的坐标
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if (obj instanceof Coord)
		{
			Coord c = (Coord) obj;
			return x == c.x && y == c.y;
		}
		return false;
	}


}
