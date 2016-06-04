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
	public boolean equals(Object that)
	{
		if (this == that) return true;
		if (that == null || ! (that instanceof Point)) return false;
		
		Point point = (Point) that;
		return this.x == point.x && this.y == point.y;
	}
	
	@Override
	public String toString() {
		return "Point { x: " + this.x + ", y: " + this.y + " }";
	}
	
	@Override
	public int hashCode() {
		return (this.x << 16) | this.y;
	}
	
}
