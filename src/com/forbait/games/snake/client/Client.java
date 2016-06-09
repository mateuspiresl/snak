package com.forbait.games.snake.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.forbait.games.snake.Command;
import com.forbait.games.snake.Command.Type;
import com.forbait.games.snake.elements.Element;
import com.forbait.games.snake.elements.Movement;
import com.forbait.games.util.Dimension;

public class Client implements Runnable {

	private Socket server;
	private ClientGame game;
	private ObjectOutputStream oos;
	
	public Client(String hostAddress, int port) throws IOException
	{
		this.server = new Socket(hostAddress, port);
		this.oos = new ObjectOutputStream(this.server.getOutputStream());
	}
	
	public void sendCommand(Command cmd) throws IOException {
		this.oos.writeObject(cmd);
		System.out.println(cmd);
	}
	
	public void sendMovement(Movement movement)
	{
		try {
			sendCommand(new Command(Type.MOVEMENT, movement));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		try {
			this.oos.close();
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		ObjectInputStream ois = null;
		
		try {
//			this.oos.writeObject(new Command(Type.START));
			
			ois = new ObjectInputStream(this.server.getInputStream());
			
			while (true) try
			{
				Command cmd = (Command) ois.readObject();
				
				switch (cmd.type)
				{
				case DIMENSION:
					this.game = new ClientGame((Dimension) cmd.data);
					this.game.setPlayer(new ClientPlayer(this));
					break;
				
				case SNAKE:
					this.game.start();
					this.game.step(new Element[] { (Element) cmd.data });
					break;
					
				case START:
					// TODO
					break;
					
				case FRAME:
					this.game.step((Element[]) cmd.data);
					break;
					
				case ERROR:
					// TODO
					return;
					
				case END:
					return;
					
				default:
				}
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ois != null) try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
