package com.forbait.games.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JPanel;

import com.forbait.games.snake.elements.Eatable;
import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.elements.Snake.Movement;
import com.forbait.games.snake.elements.SnakePiece;
import com.forbait.games.snake.exceptions.FullWorldException;
import com.forbait.games.snake.exceptions.OccupiedCellException;
import com.forbait.games.util.Dimension;
import com.forbait.games.util.Point;

@SuppressWarnings("serial")
public class World extends JPanel {

	public static final int MULTIPLIER = 14;
	
	private List<Snake> snakes = new ArrayList<Snake>();
	private Map<Point, Snake> bodies = new HashMap<Point, Snake>();
	private Map<Point, Eatable> eatables = new HashMap<Point, Eatable>();
	private Map<Snake, Movement> futureMovements = new HashMap<Snake, Movement>();
	
	private Dimension tiles;
	private Dimension screen;
	
	public World(Dimension tiles)
	{
		this.tiles = tiles;
		this.screen = new Dimension(tiles.getWidth() * MULTIPLIER, tiles.getHeight() * MULTIPLIER);
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
	
	public void setMovement(Snake snake, Snake.Movement movement) {
		this.futureMovements.put(snake, movement);
	}
	
	public Movement getMovement(Snake snake) {
		return this.futureMovements.get(snake);
	}
	
	public Snake at(Point position) {
		return this.bodies.get(position);
	}
	
	public boolean isIn(Point position) {
		return this.tiles.contains(position);
	}
	
	public Set<Snake> move()
	{
		Set<Snake> destroy = new HashSet<Snake>();
		List<Snake> cut = new ArrayList<Snake>();
		Map<Point, Snake> futureHeads = new HashMap<Point, Snake>();
		
		for (Snake snake : this.snakes)
		{
			Movement movement = this.futureMovements.get(snake);
			// System.out.println("Move: " + movement);
			
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
					// System.out.println("New move: " + movement);
					
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
			
			else if ( ! futureHeads.containsKey(headPosition))
				futureHeads.put(headPosition, snake);
			
			else {
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
		
		for (Snake snake : destroy)
		{
			remove(snake);
			
			// 20 to 80% of the body
			int numPieces = (int) (snake.getSize() * (0.2 + new Random().nextDouble() * 0.6));
			
			while (numPieces-- > 0)
				add(new SnakePiece(findEmptyCell()));
		}
		
//		for (Snake snake : this.snakes)
//			snake.move(this.futureMovements.get(snake));
		
		for (Point head : futureHeads.keySet())
		{
			Snake snake = futureHeads.get(head);
			if (destroy.contains(snake)) continue;
			
			System.out.println(futureHeads);
			System.out.println(this.eatables);
			
			if (this.eatables.containsKey(head))
			{
				this.eatables.remove(head);
				snake.eat();
			}
			else snake.move();
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
		this.futureMovements.remove(snake);
	}
	
	public void add(Snake snake)
	{
		this.snakes.add(snake);
		this.futureMovements.put(snake, snake.getMovement());
		
		for (Point point : snake.getBody())
			this.bodies.put(point, snake);		
	}
	
	public Point findEmptyCell() throws FullWorldException
	{
		int width = this.tiles.getWidth();
		int height = this.tiles.getHeight();
		
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
	
	@Override
	public java.awt.Dimension getPreferredSize() {
		return new java.awt.Dimension(this.screen.getWidth(), this.screen.getHeight());
	}
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		
		graphics.setColor(new Color(225, 255, 225));
		graphics.fillRect(0, 0, this.screen.getWidth(), this.screen.getHeight());
		
		for (Snake snake : this.snakes)
			if (snake != null)
		{
			graphics.setColor(snake.getColor());
			
			for (Point point : snake.getBody())
				graphics.fillRect(point.getX() * World.MULTIPLIER, point.getY() * World.MULTIPLIER, World.MULTIPLIER, World.MULTIPLIER);
		}
		
		for (Eatable eatable : this.eatables.values())
			eatable.draw(graphics);
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