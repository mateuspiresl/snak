package com.forbait.games.snake.elements;

import java.util.Random;

import com.forbait.games.util.Point;

public enum Movement {
	UP(1), DOWN(-1), LEFT(-2), RIGHT(2);
	
	private int id;
	
	private Movement(int id) {
		this.id = id;
	}
	
	public static Movement parse(int id)
	{
		switch (id)
		{
		case 1: return UP;
		case -1: return DOWN;
		case -2: return LEFT;
		case 2: return RIGHT;
		}
		
		return null;
	}

	public static Movement random(Random rnd)
	{
		int id = new Random().nextInt(4) - 1;
		while (id == 0) id = -2;
		
		return Movement.parse(id);
	}
	
	public static Movement random() {
		return random(new Random());
	}
	
	public Movement opposit() {
		return Movement.parse(-this.id);
	}
	
	public Movement other(Random rnd) {
		return Movement.parse((rnd.nextBoolean() ? 1 : -1) * (Math.abs(this.id) == 1 ? 2 : 1)); 
	}
	
	public Movement other() {
		return other(new Random());
	}
	
	public Point from(Point point)
	{
		switch (this)
		{
		case UP: return new Point(point.x, point.y - 1);
		case DOWN: return new Point(point.x, point.y + 1);
		case LEFT: return new Point(point.x - 1, point.y);
		case RIGHT: return new Point(point.x + 1, point.y);
		}
		
		return null;
	}
}