package com.forbait.games.snake.elements;

import com.forbait.games.util.Point;

public abstract class Eatable extends Element {

	private Point position;

	public Eatable(Point position) {
		this.position = position;
	}
	
	public Point getPosition() {
		return position;
	}
	
}
