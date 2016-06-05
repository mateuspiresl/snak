package com.forbait.games.util;

public class Point extends Pair<Integer, Integer> {

	public final int x, y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Integer getA() {
		return this.x;
	}

	@Override
	public Integer getB() {
		return this.y;
	}
	
	@Override
	public String toString() {
		return "(Point) " + super.toString();
	}
	
}
