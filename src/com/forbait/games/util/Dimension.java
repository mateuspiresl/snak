package com.forbait.games.util;

public class Dimension extends Pair<Integer, Integer> {

	public final int width, height;
	
	public Dimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public Integer getA() {
		return this.width;
	}

	@Override
	public Integer getB() {
		return this.height;
	}
	
	public boolean contains(Point point)
	{
		return 	point.x >= 0 &&
				point.x < this.width &&
				point.y >= 0 &&
				point.y < this.height;
	}

}
