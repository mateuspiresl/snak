package com.forbait.games.snake.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.forbait.games.snake.Command;
import com.forbait.games.snake.Snake;
import com.forbait.games.snake.World;
import com.forbait.games.snake.World.InvalidMovementException;

public class Client implements Runnable {

	private Socket client;
	
	private Server server;
	private World world;
	private Snake snake;
	
	private ObjectOutputStream oos;
	
	public Client(Socket client, Server server, World world) throws IOException {
		this.client = client;
		this.server = server;
		this.world = world;
		
		this.oos = new ObjectOutputStream(this.client.getOutputStream());
	}
	
	public void setSnake(Snake snake) {
		this.snake = snake;
	}
	
	public Snake getSnake() {
		return this.snake;
	}
	
	//public void sendSnake
	
	@Override
	public void run()
	{
		if (this.snake == null)
			throw new NotInitializedSnakeException();
		
		try
		{
			ObjectInputStream ois = new ObjectInputStream(this.client.getInputStream());
			
			oos.writeObject(this.snake);
			
			while (true)
			{
				try
				{
					Command cmd = (Command) ois.readObject();
					
					switch (cmd.type)
					{
					case MOVEMENT:
						try {
							this.world.move(this.snake, Snake.Movement.parse((Integer) cmd.data));
							this.server.notifyClients(this.snake);
						} catch (InvalidMovementException ime) {
							oos.writeObject(new InvalidMovementException());
						}
					}
				}
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("serial")
	public static class NotInitializedSnakeException extends RuntimeException { }
	
}
