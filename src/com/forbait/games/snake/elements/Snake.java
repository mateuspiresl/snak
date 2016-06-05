package com.forbait.games.snake.elements;

import java.awt.Color;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.forbait.games.util.Point;

public class Snake implements Serializable {

	private static int idGenerator = 1; 
	
	private transient int id;
	private Color color;
	private LinkedList<Point> body = new LinkedList<Point>();
	private transient Movement movement;
	private transient Movement nextMovement;
	
	public Snake(Color color, Point initial, Movement movement)
	{
		this.id = idGenerator++;
		this.color = color;
		this.body.add(initial);
		this.movement = movement;
		this.nextMovement = movement;
	}
	
	public Snake(Color color, Point initial) {
		this(color, initial, Movement.random());
	}
	
	public Snake(Color color) {
		this(color, new Point(0, 0));
	}
	
	@Override
	public boolean equals(Object that) {
		return this == that || (that instanceof Snake && ((Snake) that).id == this.id);
	}
	
	public int getID() {
		return this.id;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public int getSize() {
		return this.body.size();
	}
	
	public List<Point> getBody() {
		return this.body;
	}
	
	public boolean isAt(Point point) {
		return this.body.contains(point);
	}
	
	public Point getHead() {
		return this.body.getFirst();
	}
	
	public Point getTail() {
		return this.body.getLast();
	}
	
	public Movement getMovement() {
		return this.movement;
	}
	
	public synchronized Movement getNextMovement() {
		return this.nextMovement;
	}
	
	public synchronized void setNextMovement(Movement movement)
	{
		if ( ! movement.equals(this.movement.opposit()))
			this.nextMovement = movement;
	}
	
	public void eat() {
		this.body.addFirst(this.nextMovement.from(this.body.getFirst()));
		this.movement = this.nextMovement;
	}
	
	public void move()
	{
		eat();
		this.body.removeLast();
	}
	
	public void move(Movement movement)
	{
		this.nextMovement = movement;
		move();
	}
	
	@Override
	public String toString() {
		return "Snake { "
				+ "id: " + this.id + ", "
				+ "movement: " + this.movement + ", "
				+ "body: " + this.body + " }";
	}
	
	public static enum Movement {
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

		public static Movement random()
		{
			int id = new Random().nextInt(4) - 1;
			while (id == 0) id = -2;
			
			return Movement.parse(id);
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
	
}
