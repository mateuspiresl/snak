package com.forbait.games.snake.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.forbait.games.snake.server.HostWorld;
import com.forbait.games.util.Point;

public class Snake extends Element implements Serializable {

	private static final long serialVersionUID = -180974531302600129L;
	private static int idGenerator = 1;
	public static Lock movementLocker = new ReentrantLock(); 
	
	private transient int id;
	private final Color headColor;
	private final Color bodyColor;
	private volatile LinkedList<Point> body = new LinkedList<Point>();
	private transient Movement movement;
	private transient Movement nextMovement;
	
	public Snake(Color color, Point initial, Movement movement)
	{
		this.id = idGenerator++;
		this.headColor = color.darker();
		this.bodyColor = color;
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
	
	public Snake(Snake that)
	{
		this.headColor = that.headColor;
		this.bodyColor = that.bodyColor;
		this.body = new LinkedList<Point>(that.body);
	}
	
	@Override
	public boolean equals(Object that) {
		return this == that || (that instanceof Snake && ((Snake) that).id == this.id);
	}
	
	public int getID() {
		return this.id;
	}
	
	public Color getHeadColor() {
		return this.headColor;
	}
	
	public Color getBodyColor() {
		return this.bodyColor;
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
	
	public Movement getNextMovement() {
		return this.nextMovement;
	}
	
	/* Set the movement for next frame */
	public void setNextMovement(Movement movement)
	{
		Snake.movementLocker.lock();
		{
			if ( ! movement.equals(this.movement.opposit()))
				this.nextMovement = movement;
		}
		Snake.movementLocker.unlock();
	}
	
	/* Insert point as head */
	public void eat() {
		this.body.addFirst(this.nextMovement.from(this.body.getFirst()));
		this.movement = this.nextMovement;
	}
	
	/* Insert point and remove tail */
	public void move()
	{
		eat();
		this.body.removeLast();
	}
	
	@Override
	public String toString() {
		return "Snake { "
				+ "id: " + this.id + ", "
				+ "movement: " + this.movement + ", "
				+ "body: " + this.body + " }";
	}
	
	public void drawPiece(Graphics2D graphics, Point position)
	{
		position = Element.normalizedPosition(position, HostWorld.MULTIPLIER);
		graphics.fillRect(position.x, position.y, HostWorld.MULTIPLIER, HostWorld.MULTIPLIER);
	}
	
	@Override
	public void draw(Graphics2D graphics)
	{
		Iterator<Point> it = this.body.iterator();
		it.next();
		
		graphics.setColor(this.bodyColor);
		while (it.hasNext())
			drawPiece(graphics, it.next());
		
		graphics.setColor(this.headColor);
		drawPiece(graphics, getHead());
	}
	
}
