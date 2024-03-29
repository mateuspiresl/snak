package com.forbait.games.snake.elements;

import java.awt.Color;
import java.awt.Graphics2D;

import com.forbait.games.snake.server.HostWorld;
import com.forbait.games.util.Point;

@SuppressWarnings("serial")
public class SnakePiece extends Eatable {

	private Color color = Color.decode("#51b46d");
	
	public SnakePiece(Point position) {
		super(position);
	}

	@Override
	public void draw(Graphics2D graphics)
	{
		Point normalized = super.normalizedPosition(super.getPosition(), HostWorld.MULTIPLIER);
		
		graphics.setColor(this.color);
		graphics.fillRect(normalized.x, normalized.y, HostWorld.MULTIPLIER, HostWorld.MULTIPLIER);
	}

}
