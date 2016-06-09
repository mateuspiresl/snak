package com.forbait.games.snake.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.forbait.games.snake.Command;
import com.forbait.games.snake.Command.Type;
import com.forbait.games.snake.elements.Element;
import com.forbait.games.snake.elements.Movement;
import com.forbait.games.snake.ui.MessagePanel;
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
		System.out.println("Client.sendC: Sending " + cmd);
		this.oos.writeObject(cmd);
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
			ois = new ObjectInputStream(this.server.getInputStream());
			
			while (true) try
			{
				boolean closed = false;
				Command cmd = (Command) ois.readObject();
				
				System.out.println("Client.run: Command recevied: " + cmd.type);
				
				switch (cmd.type)
				{
				case DIMENSION:
					System.out.println("Client.run: Creating game");
					this.game = new ClientGame((Dimension) cmd.data, this);
					break;
				
				case SNAKE:
					System.out.println("Client.run: Showing local snake");
					this.game.start();
					this.game.step(new Element[] { (Element) cmd.data });
					break;
					
				case START:
					// TODO
					break;
					
				case FRAME:
					System.out.println("Client.run: New frame");
					this.game.step((Element[]) cmd.data);
					break;
					
				case DEAD:
					System.out.println("Client.run: Local snake is dead");
					closed = JOptionPane.showConfirmDialog(null,
							new MessagePanel().add("Your snake is dead! :(").add("Continue?"),
							"Dead", JOptionPane.YES_NO_OPTION
						) != JOptionPane.YES_OPTION;
					break;
					
				case ERROR:
					System.out.println("Client.run: Server problem");
					
					Object message = "Server problem! :(";
					
					if (cmd.data != null && cmd.data instanceof String)
						message = new MessagePanel().add((String) message).add((String) cmd.data);
										
					JOptionPane.showConfirmDialog(null, message, "Error", JOptionPane.DEFAULT_OPTION);
					
				case END:
					System.out.println("Client.run: Game ended");
					JOptionPane.showConfirmDialog(null, "Game over!", "Snak", JOptionPane.DEFAULT_OPTION);
					closed = true;
					
				default:
				}
				
				if (closed) {
					System.out.println("Client.run: Closing client and game");
					close();
					game.close();
					break;
				}
			}
			catch (ClassNotFoundException e) {
				System.out.println("Client.run: Connection closed!");
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
