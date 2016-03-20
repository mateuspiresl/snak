package com.forbait.games.snake;

import com.forbait.games.snake.server.Server.UnsetServerException;
import com.forbait.games.util.ImmutablePoint;

public class World {
	
	public static final World INSTANCE = new World();
	private Snake[][] world;
	
	private World() { }
	
	public static World get()
	{
		if (INSTANCE.world == null)
			throw new UnsetServerException();
		else
			return INSTANCE;
	}
	
	public static void set(int width, int height)
	{
		INSTANCE.world = new Snake[height][];
		
		for(int i = 0; i < INSTANCE.world.length; i++)
			INSTANCE.world[i] = new Snake[width];
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
