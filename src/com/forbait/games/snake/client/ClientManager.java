package com.forbait.games.snake.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.forbait.games.snake.Command;
import com.forbait.games.snake.Command.Type;
import com.forbait.games.snake.Debug;
import com.forbait.games.snake.elements.Element;
import com.forbait.games.snake.elements.Movement;
import com.forbait.games.snake.ui.Dialog;
import com.forbait.games.snake.ui.MessagePanel;
import com.forbait.games.util.Dimension;

public class ClientManager implements Runnable {

	private Socket server;
	private ClientGame game;
	private ObjectOutputStream oos;
	
	public ClientManager(String hostAddress, int port) throws IOException
	{
		this.server = new Socket(hostAddress, port);
		this.oos = new ObjectOutputStream(this.server.getOutputStream());
	}
	
	public void sendCommand(Command cmd) throws IOException {
		Debug.log("Client.sendC: Sending " + cmd);
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
				
				Debug.log("Client.run: Command recevied: " + cmd.type);
				
				switch (cmd.type)
				{
				case DIMENSION:
					Debug.log("Client.run: Creating game");
					this.game = new ClientGame((Dimension) cmd.data, this);
					break;
				
				case SNAKE:
					Debug.log("Client.run: Showing local snake");
					this.game.start();
					this.game.step(new Element[] { (Element) cmd.data });
					break;
					
				case START:
					Debug.log("Client.run: Starting game");
					break;
					
				case FRAME:
					Debug.log("Client.run: New frame");
					this.game.step((Element[]) cmd.data);
					break;
					
				case DEAD:
					Debug.log("Client.run: Local snake is dead");
					Dialog.nonBlockingMessage("Dead", new MessagePanel()
							.add("Your snake is dead! :(")
							.add("Continue?")
						);
					break;
					
				case ERROR:
					Debug.log("Client.run: Server problem");
					String message = "Server problem! :(";
					
					if (cmd.data != null && cmd.data instanceof String)
						Dialog.message("Error", new MessagePanel()
								.add((String) message)
								.add((String) cmd.data)
							);
					else
						Dialog.message("Error", message);
					break;
					
				case END:
					Debug.log("Client.run: Game ended");
					Dialog.message("Snak", "Game over!");
					closed = true;
					
				default:
				}
				
				if (closed) {
					Debug.log("Client.run: Closing client and game");
					close();
					game.close();
					break;
				}
			}
			catch (ClassNotFoundException e) {
				Debug.log("Client.run: Connection closed!");
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
