package com.forbait.games.snake.elements;

import java.awt.Color;
import java.util.Random;

import com.forbait.games.snake.Debug;
import com.forbait.games.snake.server.HostWorld;
import com.forbait.games.util.Dimension;
import com.forbait.games.util.Point;

@SuppressWarnings("serial")
public class Bot extends Snake {
	
	private transient HostWorld world;
	
	public Bot(Color color, Point initial, Movement movement, HostWorld world) {
		super(color, initial, movement);
		this.world = world;
	}
	
	public Bot(Color color, Point initial, HostWorld world) {
		this(color, initial, Movement.random(), world);
	}
	
	public void generateNextMovement()
	{
		Point head = super.getHead(); 
		Dimension tiles = this.world.getTiles();
		Movement movement;
		
//		movement = movementTo(findCloserEatable());
//		if (movement != null && tiles.contains(movement.from(head)))
//		{
//			super.setNextMovement(movement);
//			return;
//		}
		
		movement = super.getMovement();
		
		if (tiles.contains(movement.from(head)))
		{
			Dimension translatedTiles = new Dimension(tiles.width / 2, tiles.height / 2);
			Point translatedHead = new Point(Math.abs(head.x - translatedTiles.width), Math.abs(head.y - translatedTiles.height));
			
			float chance = 0.4F * (translatedHead.x * translatedHead.x + translatedHead.y * translatedHead.y)
					/ (translatedTiles.width * translatedTiles.width + translatedTiles.height * translatedTiles.height);
			
			Random rnd = new Random();
			
			if (rnd.nextDouble() > chance)
				return;
		}
		
		movement = movement.other();
		
		if ( ! tiles.contains(movement.from(head)))
			movement = movement.opposit();
		
		super.setNextMovement(movement);
	}
	
	public Movement movementTo(Point position)
	{
		Movement movement = null;
		Random rnd = new Random();
		
		int xDiff = super.getHead().x - position.x; 
		if (xDiff != 0)
		{
			movement = xDiff > 0 ? Movement.LEFT : Movement.RIGHT;
			Debug.log("Bot.movementT: (" + super.getID() + ") Mote to " + movement);
			
			Element occupying = this.world.occupying(movement.from(super.getHead())); 
			if (occupying != null && ! occupying.equals(this))
			{
				Debug.log("Bot.movementT: (" + super.getID() + ") Possible collision going " + movement);
				movement = null;
			}
		}
		
		int yDiff = super.getHead().y - position.y;
		if (yDiff != 0 && (movement == null || rnd.nextBoolean()))
		{
			movement = yDiff > 0 ? Movement.UP : Movement.DOWN;
			Debug.log("Bot.movementT: (" + super.getID() + ") Move to " + movement);
			
			Element occupying = this.world.occupying(movement.from(super.getHead())); 
			if (occupying != null && ! occupying.equals(this))
			{
				Debug.log("Bot.movementT: (" + super.getID() + ") Possible collision going " + movement);
				movement = null;
			}
		}
		
		return movement;
	}
	
	public Point findCloserEatable()
	{
		int distance = Integer.MAX_VALUE;
		Point position = null;
		
		for (Eatable eatable : this.world.getEatables())
		{
			int eatableDistance = super.getHead().distanceBetween(eatable.getPosition());
			
			if (eatableDistance < distance)
			{
				Debug.log("Bot.findCE: (" + super.getID() + ") Found " + eatable);
				distance = eatableDistance;
				position = eatable.getPosition();
			}
		}
		
		Debug.log("Bot.findCE: (" + super.getID() + ") Mote to " + position);
		return position;
	}
	
	@Override
	public String toString() {
		return "(Bot) " + super.toString();
	}

}
