package com.forbait.games.snake.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.forbait.games.snake.Command;
import com.forbait.games.snake.Command.Type;
import com.forbait.games.snake.elements.Element;
import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.server.HostClient.Sender;
import com.forbait.games.snake.ui.ClientsConnectionListener;
import com.forbait.games.util.Dimension;

public class Server {
	
	private ServerSocket server;
	private ExecutorService executor;
	private ExecutorService sender;
	private List<HostClient> clients = new ArrayList<HostClient>();
	
	private int numClients;
	
	public Server(int port, int numClients) throws IOException
	{
		this.server = new ServerSocket(port);
		this.executor = Executors.newFixedThreadPool(numClients);
		this.sender = Executors.newFixedThreadPool(numClients);
		this.numClients = numClients;
	}
	
	public void waitClients(HostGame game, Dimension tiles, ClientsConnectionListener listener)
	{
		listener.setHostAddress(this.server.getInetAddress().getHostName());
		listener.setClientsCounter(this.numClients, 0);
		
		while (this.clients.size() < this.numClients) try
		{
			System.out.println("Server.waitC: waiting...");
			
			HostClient client = new HostClient(this.server.accept(), tiles);
			client.setSnake(game.createSnake(Snake.class));
			
			System.out.println("Server.waitC: new connection");
			
			this.clients.add(client);
			listener.clientConnected();
		}
		catch (IOException ioe) {
			System.out.println("Server.waitC: connection fail");
		}
	}
	
	public void start()
	{
		System.out.println("Server.start");
		for (HostClient client : this.clients)
			this.executor.submit(client);
	}
	
	public void sendFrame(Element[] elements) {
		sendCommand(new Command(Type.FRAME, elements));
	}
	
	public void sendCommand(Command cmd)
	{
		for (HostClient client : this.clients)
		{
			Sender sender = client.getSender();
			sender.setCommand(cmd);
			this.sender.submit(sender);
		}
	}
	
	public void close()
	{
		System.out.println("Server.close");
		for (HostClient client : this.clients)
			client.close();
		
		try {
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
