package com.forbait.games.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import com.forbait.games.snake.elements.Eatable;
import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.elements.Snake.Movement;
import com.forbait.games.util.Point;

@SuppressWarnings("serial")
public class World extends JPanel {

	public static final int MULTIPLIER = 10;
	
	private List<Snake> snakes = new ArrayList<Snake>();
	private Map<Point, Snake> bodies = new HashMap<Point, Snake>();
	private Map<Point, Eatable> eatables = new HashMap<Point, Eatable>();
	private Map<Snake, Movement> futureMovements = new HashMap<Snake, Movement>();
	
	private int horizontalTiles, verticalTiles;
	private int width, height;
	
	public World(int horizontalTiles, int verticalTiles)
	{
		this.horizontalTiles = horizontalTiles;
		this.verticalTiles = verticalTiles;
		this.width = horizontalTiles * MULTIPLIER;
		this.height = verticalTiles * MULTIPLIER;
	}
	
	public int getHorizontalTiles() {
		return this.horizontalTiles;
	}
	
	public int getVerticalTiles() {
		return this.verticalTiles;
	}
	
	public int getWidth() {
		return this.width * MULTIPLIER;
	}
	
	public int getHeight() {
		return this.height * MULTIPLIER;
	}
	
	public List<Snake> getSnakes() {
		return this.snakes;
	}
	
	public void addMovement(Snake snake, Snake.Movement movement) {
		this.futureMovements.put(snake, movement);
	}
	
	public Movement getMovement(Snake snake) {
		return this.futureMovements.get(snake);
	}
	
	public Snake at(Point position) {
		return this.bodies.get(position);
	}
	
	public boolean isIn(Point position) {
		return 	position.getX() >= 0 &&
				position.getX() < this.width &&
				position.getY() >= 0 &&
				position.getY() < this.height;
	}
	
	public Set<Snake> move()
	{
		Set<Snake> destroy = new HashSet<Snake>();
		List<Snake> cut = new ArrayList<Snake>();
		Map<Point, Snake> futureHeads = new HashMap<Point, Snake>();
		
		for (Snake snake : this.snakes)
		{
			Movement movement = this.futureMovements.get(snake);
			System.out.println("Move: " + movement);
			
			Point headPosition = movement.from(snake.getHead());
			Snake enemy = this.bodies.get(headPosition);
			
			if (enemy != null && enemy.equals(snake))
			{
				if (snake.getBody().get(1).equals(headPosition))
				{
					if (snake.getMovement().equals(movement))
						movement = movement.opposit();
					else
						movement = snake.getMovement();
					
					this.futureMovements.put(snake, movement);
					System.out.println("New move: " + movement);
					
					headPosition = movement.from(snake.getHead());
					enemy = this.bodies.get(headPosition);
				}
				else
				{
					snake.setMovement(movement);
					continue;
				}
			}
			
			snake.setMovement(movement);
			
			if ( ! isIn(headPosition))
				cut.add(snake);
			
			else if (enemy != null && ! enemy.getTail().equals(headPosition))
				destroy.add(snake);
			
			else if (futureHeads.containsKey(headPosition))
			{
				destroy.add(snake);
				destroy.add(futureHeads.get(headPosition));
			}
		}
		
		for (Snake snake : cut)
		{
			snake.getBody().remove(0);
			
			if (snake.getSize() < 2)
				destroy.add(snake);
		}
		
		for (Snake snake : cut)
			remove(snake);
		
		for (Snake snake : this.snakes)
			snake.move(this.futureMovements.get(snake));
		
		return destroy;
	}
	
	public void remove(List<Point> body)
	{
		for (Point point : body)
			this.bodies.remove(point);
	}
	
	public void remove(Snake snake)
	{
		remove(snake.getBody());
		this.snakes.remove(snake);
		this.futureMovements.remove(snake);
	}
	
	public void add(Snake snake)
	{
		this.snakes.add(snake);
		this.futureMovements.put(snake, snake.getMovement());
		
		for (Point point : snake.getBody())
			this.bodies.put(point, snake);		
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.width, this.height);
	}
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		
		System.out.println("Drawing " + this.snakes.get(0));
		
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, this.width, this.height);
		
		for (Snake snake : this.snakes)
			if (snake != null)
		{
			graphics.setColor(snake.getColor());
			
			for (Point point : snake.getBody())
				graphics.fillRect(point.getX() * World.MULTIPLIER, point.getY() * World.MULTIPLIER, World.MULTIPLIER, World.MULTIPLIER);
		}
	}
	
}

/*public Set<Snake> makeFuture()
{
	Map<Point, Snake> heads = new HashMap<Point, Snake>();
	Map<Point, Snake> bodies = new HashMap<Point, Snake>();
	Set<Snake> collisions = new HashSet<Snake>();
	
	for (Snake snake : this.snakes)
	{
		Movement movement = this.futureMovements.get(snake);
		Point head = movement.from(snake.getHead());
		Snake enemy = heads.get(head);
		
		if (enemy == null)
			heads.put(head, snake);
		else
		{
			if (snake.getSize() <= enemy.getSize())
				collisions.add(snake);
			else
				heads.put(head, snake);
			
			if (enemy.getSize() <= snake.getSize())
				collisions.add(enemy);
		}
	}
	
	for (Point head : heads.keySet())
	{
		
	}
	
	return collisions;
}*/