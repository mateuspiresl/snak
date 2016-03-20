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
	private Snake snake;
	
	private ObjectOutputStream oos;
	
	public Client(Socket client) throws IOException
	{
		this.client = client;
		this.oos = new ObjectOutputStream(this.client.getOutputStream());
	}
	
	public void setSnake(Snake snake) {
		this.snake = snake;
	}
	
	public Snake getSnake() {
		return this.snake;
	}
	
	public void sendSnake(Snake snake) throws IOException {
		this.oos.writeObject(snake);
	}
	
	@Override
	public void run()
	{
		if (this.snake == null)
			throw new UnsetSnakeException();
		
		try {
			ObjectInputStream ois = new ObjectInputStream(this.client.getInputStream());
			sendSnake(this.snake);
			
			while (true) {
				try {
					Command cmd = (Command) ois.readObject();
					
					switch (cmd.type)
					{
					case MOVEMENT:
						try {
							World.get().move(this.snake, Snake.Movement.parse((Integer) cmd.data));
							Server.get().sendSnake(this.snake);
						} catch (InvalidMovementException ime) {
							oos.writeObject(new InvalidMovementException());
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("serial")
	public static class UnsetSnakeException extends RuntimeException { }
	
}
