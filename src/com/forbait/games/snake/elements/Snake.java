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
	
	public Snake(Color color, Point initial, Movement movement)
	{
		this.id = idGenerator++;
		this.color = color;
		this.body.add(initial);
		this.movement = movement;
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
	
	public /*Point[]*/ List<Point> getBody() {
		return this.body; //.toArray(new Point[0]);
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
	
	public void setMovement(Movement movement) {
		this.movement = movement;
	}
	
	public void position(List<Point> body)
	{
		this.body.clear();
		this.body.addAll(body);
	}
	
	public void eat() {
		this.body.addFirst(movement.from(this.body.getFirst()));
	}
	
	public void move()
	{
		eat();
		this.body.removeLast();
	}
	
	public void move(Movement movement)
	{
		this.movement = movement;
		move();
	}
	
	/*public void eat(Point point)
	{
		// TODO check is possible
		
		this.body.add(point);
	}
	
	public Point[] breakAt(int index)
	{
		if (index < 0 || index >= this.body.size())
			return null;
		
		List<Point> remove = this.body.subList(index, this.body.size());
		Point[] removed = remove.toArray(new Point[0]);
		remove.clear();
		
		return removed;
	}
	
	public Point[] breakAt(Point point) {
		return breakAt(this.body.indexOf(point));
	}*/
	
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
		
		public Point from(Point point)
		{
			switch (this)
			{
			case UP: return new Point(point.getX(), point.getY() - 1);
			case DOWN: return new Point(point.getX(), point.getY() + 1);
			case LEFT: return new Point(point.getX() - 1, point.getY());
			case RIGHT: return new Point(point.getX() + 1, point.getY());
			}
			
			return null;
		}
	}
	
}
