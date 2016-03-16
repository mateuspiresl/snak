package com.forbait.games.snake;

import com.forbait.games.util.ImmutablePoint;

public class World {
	
	private Snake[][] world;
	
	public World(int width, int height)
	{
		this.world = new Snake[height][];
		
		for(int i = 0; i < this.world.length; i++)
			this.world[i] = new Snake[width];
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
	
	public static class InvalidMovementException extends Exception {
		// Generated serial version
		private static final long serialVersionUID = -8090115647167755731L;
	}
	
}
