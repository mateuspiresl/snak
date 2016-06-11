package com.forbait.games.snake.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import com.forbait.games.snake.Command;
import com.forbait.games.snake.Command.Type;
import com.forbait.games.snake.DB;
import com.forbait.games.snake.Debug;
import com.forbait.games.snake.Program;
import com.forbait.games.snake.elements.Element;
import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.server.HostClient.Sender;
import com.forbait.games.snake.ui.ClientsConnectionListener;
import com.forbait.games.util.Dimension;
import com.forbait.games.snake.matchserver.MatchInfo;

import io.orchestrate.client.KvMetadata;

public class HostManager {
	
	private ServerSocket server;
	private ExecutorService executor;
	private ExecutorService sender;
	private List<HostClient> clients = new ArrayList<HostClient>();
	
	private Socket matchServer;
	private int numClients;
	private boolean closed = false;
	
	public HostManager(int port, int numClients) throws BindException, IOException
	{
		this.server = new ServerSocket(port);
		this.executor = Executors.newFixedThreadPool(numClients);
		this.sender = Executors.newFixedThreadPool(numClients);
		this.numClients = numClients;
	}
	
	public void waitClients(HostGame game, Dimension tiles, ClientsConnectionListener listener)
	{
		listener.setClientsCounter(this.numClients, 0);
		
		while (this.clients.size() < this.numClients) try
		{
			Debug.log("Server.waitC: Waiting...");
			
			HostClient client = new HostClient(this.server.accept(), tiles);
			client.setSnake(game.createSnake(Snake.class));
			
			Debug.log("Server.waitC: New connection");
			
			this.clients.add(client);
			listener.clientConnected();
			
			try {
				this.matchServer.close();
			} catch (IOException ioe) {
				Debug.log("Server.waitC: Macth server onnection already closed");
				ioe.printStackTrace();
			}
		}
		catch (IOException ioe) {
			if (this.closed)
			{
				Debug.log("Server.waitC: Connection closed");
				break;
			}
			else
				Debug.log("Server.waitC: Connection fail");
		}
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

	public void notifyMatchServer(String name)
	{
		MatchInfo info = new MatchInfo("", Program.HOST_PORT, name);
		
		final KvMetadata kvMetadata = DB.client.kv("hosts", "host")
			.put(info)
			.get();
		
		try {
			this.matchServer = new Socket(Program.MATCH_SERVER_ADDRESS, Program.MATCH_SERVER_PORT);
			
			ObjectOutputStream oos = new ObjectOutputStream(matchServer.getOutputStream());
			oos.writeObject(new Command(Command.Type.SERVER, new MatchInfo("", Program.HOST_PORT, name)));
			
//			oos.close();
		} catch (UnknownHostException e) {
			Debug.log("Could not connect to match server");
			e.printStackTrace();
			if (askLocalGame()) this.close();
		} catch (IOException e) {
			Debug.log("Connection error on match server");
			e.printStackTrace();
			if (askLocalGame()) this.close();
		}
	}
	
	public boolean askLocalGame()
	{
		return JOptionPane.showConfirmDialog(null,
				"Could not connect to match server. Start local game?", "Error",
				JOptionPane.YES_NO_OPTION, JOptionPane.YES_OPTION
			) != JOptionPane.YES_OPTION;
	}
	
}
