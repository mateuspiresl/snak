package com.forbait.games.snake.elements;

import java.awt.Color;
import java.awt.Graphics;

import com.forbait.games.snake.Point;
import com.forbait.games.snake.World;

public class Egg extends Eatable {

	public Egg(Point position) {
		super(position);
	}

	@Override
	public void draw(Graphics graphics)
	{
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillOval(super.getPosition().getX(), super.getPosition().getY(), World.MULTIPLIER, World.MULTIPLIER);
	}

}
