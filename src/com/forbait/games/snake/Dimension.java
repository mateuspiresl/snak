package com.forbait.games.snake;

import com.forbait.games.util.Pair;

public class Dimension extends Pair<Integer, Integer> {

	public Dimension(int width, int height) {
		super(width, height);
	}
	
	public int getWidth() {
		return super.getA();
	}
	
	public int getHeight() {
		return super.getB();
	}
	
	public boolean contains(Point point)
	{
		return 	point.getX() >= 0 &&
				point.getX() < super.getA() &&
				point.getY() >= 0 &&
				point.getY() < super.getB();
	}

}
