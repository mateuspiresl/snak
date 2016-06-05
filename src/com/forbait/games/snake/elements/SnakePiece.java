package com.forbait.games.snake.elements;

import java.awt.Color;
import java.awt.Graphics;

import com.forbait.games.snake.World;
import com.forbait.games.util.Point;

public class SnakePiece extends Eatable {

	public SnakePiece(Point position) {
		super(position);
	}

	@Override
	public void draw(Graphics graphics)
	{
		Point normalized = super.normalizedPosition(super.getPosition(), World.MULTIPLIER);
		
		graphics.setColor(Color.GREEN);
		graphics.fillRect(normalized.x, normalized.y, World.MULTIPLIER, World.MULTIPLIER);
	}

}
