package com.forbait.games.snake.elements;

import java.awt.Color;
import java.awt.Graphics2D;

import com.forbait.games.snake.World;
import com.forbait.games.util.Point;

public class Egg extends Eatable {

	public Egg(Point position) {
		super(position);
	}

	@Override
	public void draw(Graphics2D graphics)
	{
		Point normalized = super.normalizedPosition(super.getPosition(), World.MULTIPLIER);
		
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillOval(normalized.x, normalized.y, World.MULTIPLIER, World.MULTIPLIER);
	}
	
	@Override
	public String toString() {
		return "Egg { " + super.getPosition() + " }";
	}

}
