package com.forbait.games.snake.elements;

import com.forbait.games.util.Point;

public abstract class Eatable extends Element {

	private static final long serialVersionUID = -1524677216476271397L;
	
	private Point position;

	public Eatable(Point position) {
		this.position = position;
	}
	
	public Point getPosition() {
		return position;
	}
	
}
