package com.forbait.games.snake.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.forbait.games.snake.Command;
import com.forbait.games.snake.Command.Type;
import com.forbait.games.snake.elements.Element;
import com.forbait.games.snake.elements.Movement;
import com.forbait.games.snake.elements.Snake;
import com.forbait.games.util.Dimension;

public class HostClient implements Runnable {

	private Socket client;
	private Snake snake;
	private Sender sender;
	
	public HostClient(Socket client, Dimension tiles) throws IOException
	{
		this.client = client;
		this.sender = new Sender(new ObjectOutputStream(this.client.getOutputStream()));
		this.sender.send(new Command(Type.DIMENSION, tiles));
	}
	
	public Sender getSender() {
		return this.sender;
	}
	
	public Snake getSnake() {
		return this.snake;
	}
	
	public void setSnake(Snake snake) throws IOException
	{
		this.snake = snake;
		this.sender.send(new Command(Type.SNAKE, (Element) snake));
	}
	
	public void close()
	{
		System.out.println("HostC.close");
		try {
			this.sender.send(new Command(Command.Type.END));
			this.sender.close();
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		ObjectInputStream ois = null;
		
		try {
			if (this.snake == null)
			{
				System.out.println("HostC.run: has not a snake");
				this.sender.send(new Command(Command.Type.ERROR, "No snake."));
			}
			else
			{
				this.sender.send(new Command(Type.START));
				
				ois = new ObjectInputStream(this.client.getInputStream());
				
				while (true) {
					try {
						Command cmd = (Command) ois.readObject();
						
						if (cmd.type.equals(Command.Type.MOVEMENT))
						{
							System.out.println("HostC.run: Received movement " + cmd.data);
							this.snake.setNextMovement((Movement) cmd.data);
						}
					}
					catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					catch (IOException ioe) {
						throw ioe;
					}
					catch (Exception e) {
						System.out.println("Erro!");
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			System.out.println("HostC.run: Connection closed!");
			e.printStackTrace();
		} finally {
			if (ois != null) try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public class Sender implements Runnable {
		
		private final ObjectOutputStream oos;
		private Command cmd;
		
		public Sender(ObjectOutputStream oos) {
			this.oos = oos;
		}
		
		public void setCommand(Command cmd) {
			this.cmd = cmd;
		}
		
		public void send(Command cmd) {
			setCommand(cmd);
			run();
		}
		
		public void close() throws IOException {
			this.oos.close();
		}

		@Override
		public void run() {
			try {
				this.oos.writeObject(this.cmd);
				this.oos.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
