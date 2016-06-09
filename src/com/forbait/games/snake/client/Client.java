package com.forbait.games.snake.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
					
				case DEAD:
					JPanel content = new JPanel();
					content.add(new JLabel("Your snake is dead! :("));
					content.add(new JLabel("Continue?"));

					if (JOptionPane.showConfirmDialog(null, content, "", JOptionPane.OK_OPTION) != JOptionPane.OK_OPTION)
						closed = true;
					break;
					
				case ERROR:
					JOptionPane.showConfirmDialog(null, "Server problem! :(", "", JOptionPane.OK_OPTION);
					
				case END:
					closed = true;
					
				default:
				}
				
				if (closed) {
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
