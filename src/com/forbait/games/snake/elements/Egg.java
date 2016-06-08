package com.forbait.games.snake.elements;

import java.awt.Color;
import java.awt.Graphics2D;

import com.forbait.games.snake.server.HostWorld;
import com.forbait.games.util.Point;

public class Egg extends Eatable {

	private static final long serialVersionUID = 7476254127913963306L;

	private Color c;
	
	public Egg(Point position) {
		super(position);
	}

	@Override
	public void draw(Graphics2D graphics)
	{
		Point normalized = super.normalizedPosition(super.getPosition(), HostWorld.MULTIPLIER);
		
		c = c != null && c.equals(Color.GREEN) ? Color.blue : Color.GREEN;
		graphics.setColor(c);
//		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillOval(normalized.x, normalized.y, HostWorld.MULTIPLIER, HostWorld.MULTIPLIER);
	}
	
	@Override
	public String toString() {
		return "Egg { " + super.getPosition() + " }";
	}

}
