package com.forbait.games.snake;

import java.awt.Color;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.forbait.games.util.ImmutablePoint;

public class Snake {

	private int id;
	private Color color;
	private LinkedList<ImmutablePoint> body = new LinkedList<ImmutablePoint>();
	
	private Snake(Data data)
	{
		this.color = data.color;
		this.body = data.body;
	}
	
	public Snake(int id, Color color, ImmutablePoint initial)
	{
		this.id = id;
		this.color = color;
		this.body.add(initial);
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
	
	public ImmutablePoint[] getBody() {
		return this.body.toArray(new ImmutablePoint[0]);
	}
	
	public boolean isAt(ImmutablePoint point) {
		return this.body.contains(point);
	}
	
	public ImmutablePoint getHead() {
		return this.body.getFirst();
	}
	
	public ImmutablePoint getTail() {
		return this.body.getLast();
	}
	
	public void move(Movement movement)
	{
		this.body.add(movement.from(this.body.getFirst()));
		this.body.removeLast();
	}
	
	public void eat(ImmutablePoint point)
	{
		// TODO check is possible
		
		this.body.add(point);
	}
	
	public ImmutablePoint[] breakAt(int index)
	{
		if (index < 0 || index >= this.body.size())
			return null;
		
		List<ImmutablePoint> remove = this.body.subList(index, this.body.size());
		ImmutablePoint[] removed = remove.toArray(new ImmutablePoint[0]);
		remove.clear();
		
		return removed;
	}
	
	public ImmutablePoint[] breakAt(ImmutablePoint point) {
		return breakAt(this.body.indexOf(point));
	}
	
	public Data data() {
		return new Data(this);
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
		
		public ImmutablePoint from(ImmutablePoint point)
		{
			switch (id)
			{
			case 0: return new ImmutablePoint(point.getX(), point.getY() + 1);
			case 1: return new ImmutablePoint(point.getX(), point.getY() - 1);
			case 2: return new ImmutablePoint(point.getX() - 1, point.getY());
			case 3: return new ImmutablePoint(point.getX() + 1, point.getY());
			}
			
			return null;
		}
	}
	
	public static class Data implements Serializable {

		// Generated serial version
		private static final long serialVersionUID = -3076986268931831959L;

		public int id;
		public Color color;
		public LinkedList<ImmutablePoint> body = new LinkedList<ImmutablePoint>();
		
		public Data(Snake snake)
		{
			this.id = snake.id;
			this.color = snake.color;
			this.body = snake.body;
		}
		
		public Snake build() {
			return new Snake(this);
		}
		
	}
	
}
