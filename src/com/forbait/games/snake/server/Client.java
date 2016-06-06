package com.forbait.games.snake.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.forbait.games.snake.Command;
import com.forbait.games.snake.Command.Type;
import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.elements.Snake.Movement;

public class Client implements Runnable {

	private Socket client;
	private Snake snake;
	private ObjectOutputStream oos;
	
	public Client(Socket client) throws IOException
	{
		this.client = client;
		this.oos = new ObjectOutputStream(this.client.getOutputStream());
	}
	
	public void setSnake(Snake snake) throws IOException
	{
		this.snake = snake;
		this.oos.writeObject(snake);
	}
	
	public void sendFrame(Command frame) throws IOException {
		this.oos.writeObject(frame);
	}
	
	/*public Snake getSnake() {
		return this.snake;
	}*/
	
	@Override
	public void run()
	{
		try {
			if (this.snake == null)
			{
				System.out.println(client + " has not a snake!");
				this.oos.writeObject(new Command(Command.Type.ERROR, "No snake."));
			}
			else
			{
				this.oos.writeObject(new Command(Type.START));
				
				ObjectInputStream ois = new ObjectInputStream(this.client.getInputStream());
				
				while (true) {
					try {
						Command cmd = (Command) ois.readObject();
						
						if (cmd.type.equals(Command.Type.MOVEMENT))
							this.snake.setNextMovement(Movement.parse((Integer) cmd.data));
					}
					catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("serial")
	public static class UnsetSnakeException extends RuntimeException { }
	
}
