package com.forbait.games.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import com.forbait.games.snake.server.Server.UnsetServerException;
import com.forbait.games.util.ImmutablePoint;

@SuppressWarnings("serial")
public class World extends JPanel {
	
	public static final World INSTANCE = new World();
	
	private Snake[] snakes;
	private Snake[][] world;
	private int width, height;
	
	private World() { }
	
	public static World get()
	{
		if (INSTANCE.world == null)
			throw new UnsetServerException();
		else
			return INSTANCE;
	}
	
	public static void set(int width, int height, int numSnakes)
	{
		INSTANCE.width = width;
		INSTANCE.height = height;
		INSTANCE.snakes = new Snake[numSnakes];
		
		INSTANCE.world = new Snake[height][];
		
		for(int i = 0; i < INSTANCE.world.length; i++)
			INSTANCE.world[i] = new Snake[width];
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public Snake getSnake(int id) {
		return this.snakes[id];
	}
	
	public void setSnake(int id, Snake snake) {
		this.snakes[id] = snake;
	}
	
	public Snake at(ImmutablePoint point) {
		return this.world[point.getY()][point.getX()];
	}
	
	private void put(ImmutablePoint point, Snake snake) {
		this.world[point.getY()][point.getX()] = snake;
	}
	
	public void move(Snake snake, Snake.Movement movement) throws InvalidMovementException
	{
		ImmutablePoint newHead = movement.from(snake.getHead());
		
		try {
			if (this.at(newHead) == null)
			{
				snake.move(movement);
				this.put(newHead, snake);
				this.put(snake.getTail(), null);
			}
			else throw new InvalidMovementException();
		}
		catch (ArrayIndexOutOfBoundsException aioobe) {
			throw new InvalidMovementException();
		}
	}
	
	public void position(int id, List<ImmutablePoint> snakeBody)
	{
		Snake snake = this.snakes[id];
		
		for (ImmutablePoint point : snake.getBody())
			this.world[point.getY()][point.getX()] = null;
		
		snake.position(snakeBody);
		
		for (ImmutablePoint point : snakeBody)
			this.world[point.getY()][point.getX()] = snake;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.width, this.height);
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		graphics.setColor(Color.GREEN);
		graphics.fillRect(0, 0, JPanel.WIDTH, JPanel.HEIGHT);
		
		for (Snake snake : this.snakes)
			if (snake != null)
				snake.draw(graphics);
	}
	
	public static class InvalidMovementException extends Exception {
		// Generated serial version
		private static final long serialVersionUID = -8090115647167755731L;
	}
	
}
