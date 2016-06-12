package com.forbait.games.snake.server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import com.forbait.games.snake.Command;
import com.forbait.games.snake.Command.Type;
import com.forbait.games.snake.DB;
import com.forbait.games.snake.Debug;
import com.forbait.games.snake.elements.Element;
import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.server.HostClient.Sender;
import com.forbait.games.snake.ui.ClientsConnectionListener;
import com.forbait.games.util.Dimension;

public class HostManager {
	
	private ServerSocket server;
	private ExecutorService executor;
	private ExecutorService sender;
	private ScheduledExecutorService posterTimer = Executors.newScheduledThreadPool(1);
	private List<HostClient> clients = new ArrayList<HostClient>();

	private GameInfo info;
	private boolean closed = false;
	
	public HostManager(GameInfo info) throws BindException, IOException
	{
		this.info = info;
		this.server = new ServerSocket(info.port);
		this.executor = Executors.newFixedThreadPool(info.numPlayers - 1);
		this.sender = Executors.newFixedThreadPool(info.numPlayers - 1);
	}
	
	public void waitClients(HostGame game, Dimension tiles, ClientsConnectionListener listener)
	{
		listener.setClientsCounter(this.info.numPlayers - 1, 0);
		this.posterTimer.scheduleAtFixedRate(new GamePoster(this.info), 0, 20, TimeUnit.SECONDS);

		while (this.clients.size() < this.info.numPlayers - 1) try
		{
			Debug.log("Server.waitC: Waiting...");
			
			HostClient client = new HostClient(this.server.accept(), tiles);
			client.setSnake(game.createSnake(Snake.class));
			
			Debug.log("Server.waitC: New connection");
			
			this.clients.add(client);
			listener.clientConnected();
			
			this.info.countPlayer();
		}
		catch (IOException ioe) {
			if (this.closed)
			{
				Debug.log("Server.waitC: Connection closed");
				break;
			}
			else if (this.server.isClosed())
			{
				Debug.log("Server.waitC: Internal server error");
				break;
			}
			else Debug.log("Server.waitC: Client connection error");
		}

		this.posterTimer.shutdownNow();
		DB.closePost(this.info);
	}
	
	public void start()
	{
		Debug.log("Server.start");
		for (HostClient client : this.clients)
			this.executor.submit(client);
	}
	
	public void sendFrame(Element[] elements) {
		sendCommand(new Command(Type.FRAME, elements));
	}
	
	private void sendCommand(HostClient client, Command cmd)
	{
		Debug.log("Server.sendC: Sending " + cmd + " to " + client);
		
		Sender sender = client.getSender();
		sender.setCommand(cmd);
		this.sender.submit(sender);
	}
	
	public void sendCommand(Command cmd)
	{
		for (HostClient client : this.clients)
			sendCommand(client, cmd);
	}
	
	public void notifyDeath(Snake snake)
	{
		for (HostClient client : this.clients)
			if (client.getSnake().equals(snake))
		{
			Debug.log("Server.notifyD: Notifying the client of " + client.getSnake());
			sendCommand(client, Command.DEAD);				
		}
	}
	
	public void close()
	{
		Debug.log("Server.close");
		this.closed = true;
		
		for (HostClient client : this.clients)
			client.close();
		
		try {
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean askLocalGame()
	{
		return JOptionPane.showConfirmDialog(null,
				"Could not connect to match server. Start local game?", "Error",
				JOptionPane.YES_NO_OPTION, JOptionPane.YES_OPTION
			) != JOptionPane.YES_OPTION;
	}

	private class GamePoster implements Runnable {

		final GameInfo info;
		private int numPlayersLeft;

		public GamePoster(GameInfo info) {
			this.info = info;
			this.numPlayersLeft = info.numPlayersLeft;
		}

		@Override
		public void run()
		{
			info.updateTime();
			DB.postGame(this.info);
		}

	}

}
