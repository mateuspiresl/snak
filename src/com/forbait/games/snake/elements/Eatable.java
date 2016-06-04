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
	
	public abstract void draw(Graphics graphics);
	
}
