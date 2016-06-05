package com.forbait.games.snake;

import com.forbait.games.util.Pair;

public class Point extends Pair<Integer, Integer> {

	public Point(int x, int y) {
		super(x, y);
	}
	
	public int getX() {
		return super.getA();
	}
	
	public int getY() {
		return super.getB();
	}
	
	@Override
	public String toString() {
		return "(Point) " + super.toString();
	}
	
}
