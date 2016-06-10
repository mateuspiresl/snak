package com.forbait.games.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Point extends Pair<Integer, Integer> implements Serializable {

	public final int x, y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int distanceBetween(Point that) {
		return Math.abs(this.x - that.x) + Math.abs(this.y - that.y);
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
