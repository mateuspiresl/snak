package com.forbait.games.snake;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.forbait.games.snake.elements.Eatable;
import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.elements.Snake.Movement;
import com.forbait.games.snake.elements.SnakePiece;
import com.forbait.games.snake.exceptions.FullWorldException;
import com.forbait.games.snake.exceptions.OccupiedCellException;
import com.forbait.games.util.Dimension;
import com.forbait.games.util.Point;

@SuppressWarnings("serial")
public class World extends Canvas {

	public static final int MULTIPLIER = 14;
	
	private List<Snake> snakes = new ArrayList<Snake>();
	private Map<Point, Snake> bodies = new HashMap<Point, Snake>();
	private Map<Point, Eatable> eatables = new HashMap<Point, Eatable>();
	
	private Dimension tiles;
	private Dimension screen;
	
	public World(Dimension tiles)
	{
		this.tiles = tiles;
		this.screen = new Dimension(tiles.width * MULTIPLIER, tiles.height * MULTIPLIER);
		
		super.setBackground(new Color(225, 255, 225));
		super.setSize(new java.awt.Dimension(this.screen.width, this.screen.height));
	}
	
	@Debug
	public int countOccupied() {
		return this.bodies.size() + this.eatables.size();
	}
	
	public Dimension getTiles() {
		return this.tiles;
	}

	public Dimension getScreen() {
		return this.screen;
	}
	
	public List<Snake> getSnakes() {
		return this.snakes;
	}
	
	public int countSnakes() {
		return this.snakes.size();
	}
	
	public Snake at(Point position) {
		return this.bodies.get(position);
	}
	
	public boolean isIn(Point position) {
		return this.tiles.contains(position);
	}
	
	public boolean isOccupied(Point position) {
		return this.bodies.containsKey(position) || this.eatables.containsKey(position);
	}
	
	public Set<Snake> move()
	{
		Set<Snake> destroy = new HashSet<Snake>();
		List<Snake> cut = new ArrayList<Snake>();
		Map<Snake, Point> tailCollision = new HashMap<Snake, Point>();
		Map<Point, Snake> futureHeads = new HashMap<Point, Snake>();
		
		for (Snake snake : this.snakes)
		{
			Movement movement = snake.getNextMovement();
			Point headPosition = movement.from(snake.getHead());
			Snake enemy = this.bodies.get(headPosition);

			// Useless
			/*synchronized (snake)
			{
				movement = snake.getNextMovement();
				headPosition = movement.from(snake.getHead());
				enemy = this.bodies.get(headPosition);
				
				// If collided with its own body
				if (snake.equals(enemy))
				{
					// If collided with its nuke
					// Never happens
					/*if (snake.getBody().get(1).equals(headPosition))
					{
						if (snake.getMovement().equals(movement))
							movement = movement.opposit();
						else
							movement = snake.getMovement();
						
						snake.setNextMovement(movement);
						
						headPosition = movement.from(snake.getHead());
						enemy = this.bodies.get(headPosition);
						
						if (snake.equals(enemy))
							enemy = null;
					}
					else enemy = null;
				}
			}*/
			
			if (snake.equals(enemy))
				enemy = null;
			
			// Head goes out of the field
			if ( ! isIn(headPosition))
				cut.add(snake);
			
			else if (enemy != null)
			{
				// Possible collision with enemy body if it is not eating
				if (enemy.getTail().equals(headPosition))
					tailCollision.put(snake, headPosition);
				
				// Collides with enemy body
				else
					destroy.add(snake);
			}
			
			// Does not collide with any head
			else if ( ! futureHeads.containsKey(headPosition))
				futureHeads.put(headPosition, snake);
			
			// Does
			else {
				destroy.add(snake);
				destroy.add(futureHeads.get(headPosition));
			} 
		}
		
		// Cuts snakes if its size is less than the minimum
		for (Snake snake : cut)
		{
			Point head = snake.getBody().remove(0);
			
			if (snake.getSize() < 2)
				destroy.add(snake);
			else
				futureHeads.put(head, snake);
		}
		
		// Adds snakes to destroy set if it collided with a tail of
		// an enemy that is eating
		for (Snake snake : tailCollision.keySet())
		{
			Snake enemy = this.bodies.get(tailCollision.get(snake));
			
			if (destroy.contains(enemy))
				futureHeads.put(enemy.getTail(), snake);
			
			else if (this.eatables.containsKey(enemy.getNextMovement().from(enemy.getHead())))
				destroy.add(snake);
		}
		
		// Destroys snakes and spread its pieces
		for (Snake snake : destroy)
		{
			remove(snake);
			
			// 20 to 80% of the body
			int numPieces = (int) (snake.getSize() * (0.2 + new Random().nextDouble() * 0.6));
			
			List<Point> body = snake.getBody();
			Collections.shuffle(body, new Random(System.nanoTime()));
			
			for (int i = 0; i < numPieces; i++)
				add(new SnakePiece(body.get(i)));
		}
		
		// Makes snakes move or eat
		for (Point head : futureHeads.keySet())
		{
			Snake snake = futureHeads.get(head);
			if (destroy.contains(snake)) continue;
			
			if (this.eatables.containsKey(head))
			{
				this.eatables.remove(head);
				this.bodies.put(head, snake);
				snake.eat();
			}
			else
			{
				this.bodies.remove(snake.getTail());
				this.bodies.put(head, snake);
				snake.move();
			}
		}
		
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
	}
	
	public void add(Snake snake)
	{
		this.snakes.add(snake);
		
		for (Point point : snake.getBody())
			this.bodies.put(point, snake);		
	}
	
	public Point findEmptyCell() throws FullWorldException
	{
		int width = this.tiles.width;
		int height = this.tiles.height;
		
		Random rnd = new Random();
		int x = rnd.nextInt(width);
		int y = rnd.nextInt(height);
		
		for (int xVar = 0; xVar < width; xVar++) {
			for (int yVar = 0; yVar < height; yVar++)
			{
				Point position = new Point((x + xVar) % width, (y + yVar) % height);
				if ( ! this.bodies.containsKey(position) && ! this.eatables.containsKey(position))
					return position;
			}
		}
		
		throw new FullWorldException();
	}
	
	public void add(Eatable eatable)
	{
		if ( ! this.eatables.containsKey(eatable.getPosition()))
			this.eatables.put(eatable.getPosition(), eatable);
		else
			throw new OccupiedCellException();
	}
	
	public int countEatables() {
		return this.eatables.size();
	}
	
	@Override
	public void paint(Graphics graphics)
	{
		Graphics2D g = (Graphics2D) graphics;

		for (Snake snake : this.snakes)
			snake.draw(g);
		
		for (Eatable eatable : this.eatables.values())
			eatable.draw(g);
	}
	
}