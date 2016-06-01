package com.forbait.games.util;


public class Point {

	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || ! (obj instanceof Point))
			return false;
		
		Point that = (Point) obj;
		return this.x == that.x && this.y == that.y;
	}
	
	
}
