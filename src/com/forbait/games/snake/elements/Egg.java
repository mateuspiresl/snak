package com.forbait.games.snake.elements;

import java.awt.Color;
import java.awt.Graphics2D;

import com.forbait.games.snake.server.HostWorld;
import com.forbait.games.util.Point;

@SuppressWarnings("serial")
public class Egg extends Eatable {
	
	public Egg(Point position) {
		super(position);
	}

	@Override
	public void draw(Graphics2D graphics)
	{
		Point normalized = super.normalizedPosition(super.getPosition(), HostWorld.MULTIPLIER);
		
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillOval(normalized.x, normalized.y, HostWorld.MULTIPLIER, HostWorld.MULTIPLIER);
	}
	
	@Override
	public String toString() {
		return "Egg { " + super.getPosition() + " }";
	}

}
