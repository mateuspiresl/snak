package com.forbait.games.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.forbait.games.util.Point;

public class Snake implements Serializable {

	private int id;
	private Color color;
	private LinkedList<Point> body = new LinkedList<Point>();
	
	public Snake(int id, Color color, Point initial)
	{
		this.id = id;
		this.color = color;
		this.body.add(initial);
	}
	
	@Override
	public boolean equals(Object that) {
		return that instanceof Snake && ((Snake) that).id == this.id;
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
	
	public Point[] getBody() {
		return this.body.toArray(new Point[0]);
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
	
	public void position(List<Point> body)
	{
		this.body.clear();
		this.body.addAll(body);
	}
	
	public void move(Movement movement)
	{
		this.body.add(movement.from(this.body.getFirst()));
		this.body.removeLast();
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
	
	public void draw(Graphics graphics)
	{
		graphics.setColor(this.color);
		
		for (Point point : this.body)
			graphics.fillRect(point.getX(), point.getY(), World.MULTIPLIER, World.MULTIPLIER);
	}
	
	
	public static enum Movement {
		UP(0), DOWN(1), LEFT(2), RIGHT(3);
		
		private int id;
		
		private Movement(int id) {
			this.id = id;
		}
		
		public static Movement parse(int id)
		{
			switch (id)
			{
			case 0: return UP;
			case 1: return DOWN;
			case 2: return LEFT;
			case 3: return RIGHT;
			}
			
			return null;
		}
		
		public Point from(Point point)
		{
			switch (id)
			{
			case 0: return new Point(point.getX(), point.getY() + 1);
			case 1: return new Point(point.getX(), point.getY() - 1);
			case 2: return new Point(point.getX() - 1, point.getY());
			case 3: return new Point(point.getX() + 1, point.getY());
			}
			
			return null;
		}
	}
	
}
