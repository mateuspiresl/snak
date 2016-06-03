package com.forbait.games.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.forbait.games.util.Point;

@SuppressWarnings("serial")
public class World extends JPanel {

	public static final int MULTIPLIER = 10;
	
	private List<Snake> snakes = new ArrayList<Snake>();
	private Snake[][] world;
	private int horizontalTiles, verticalTiles;
	private int width, height;
	
	public World(int horizontalTiles, int verticalTiles)
	{
		this.horizontalTiles = horizontalTiles;
		this.verticalTiles = verticalTiles;
		this.width = horizontalTiles * MULTIPLIER;
		this.height = verticalTiles * MULTIPLIER;
		
		this.world = new Snake[verticalTiles][];
		for(int i = 0; i < verticalTiles; i++)
			this.world[i] = new Snake[horizontalTiles];
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
	
	public Snake at(Point position) {
		return this.world[position.getY()][position.getX()];
	}
	
	private void put(Point point, Snake snake) {
		this.world[point.getY()][point.getX()] = snake;
	}
	
	public void move(Snake snake, Snake.Movement movement) throws InvalidMovementException
	{
		Point newHead = movement.from(snake.getHead());
		
		try {
			if (at(newHead) == null)
			{
				snake.move(movement);
				put(newHead, snake);
				put(snake.getTail(), null);
			}
			else throw new InvalidMovementException();
		}
		catch (ArrayIndexOutOfBoundsException aioobe) {
			throw new InvalidMovementException();
		}
	}
	
	public void erase()
	{
		for (Snake[] row : this.world)
			for (int i = 0; i < this.width; i++)
				row[i] = null;
	}
	
	public void remove(Point[] positions)
	{
		for (Point point : positions)
			this.world[point.getY()][point.getX()] = null;
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
			this.world[point.getY()][point.getX()] = snake;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.width, this.height);
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
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
	
	public static class InvalidMovementException extends Exception {
	}
	
}
