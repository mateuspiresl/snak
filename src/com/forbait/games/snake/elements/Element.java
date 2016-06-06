package com.forbait.games.snake.elements;

import java.awt.Graphics2D;

import com.forbait.games.util.Point;

public abstract class Element {
	
	public static Point normalizedPosition(Point position, int multiplier) {
		return new Point(position.x * multiplier, position.y * multiplier);
	}
	
	public abstract void draw(Graphics2D graphics);
	
}
