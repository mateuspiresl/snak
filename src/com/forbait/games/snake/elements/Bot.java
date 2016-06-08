package com.forbait.games.snake.elements;

import java.awt.Color;
import java.util.Random;

import com.forbait.games.snake.server.HostWorld;
import com.forbait.games.util.Dimension;
import com.forbait.games.util.Point;

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
		Movement movement = super.getMovement();
		
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
	
	@Override
	public String toString() {
		return "(Bot) " + super.toString();
	}

}
