package com.forbait.games.snake.elements;

import java.awt.Graphics;

import com.forbait.games.util.Point;

public abstract class Eatable {

	private Point position;

	public Eatable(Point position) {
		this.position = position;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public Point normalizedPosition(Point position, int multiplier) {
		return new Point(position.getX() * multiplier, position.getY() * multiplier);
	}
	
	public abstract void draw(Graphics graphics);
	
}
