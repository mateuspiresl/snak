package com.forbait.games.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Dimension extends Pair<Integer, Integer> implements Serializable {

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
