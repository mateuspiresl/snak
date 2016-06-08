package com.forbait.games.util;

import java.io.Serializable;

public class Point extends Pair<Integer, Integer> implements Serializable {

	private static final long serialVersionUID = -5804613612181902064L;
	
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
