package com.forbait.games.snake.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.forbait.games.snake.Command;
import com.forbait.games.snake.Command.Type;
import com.forbait.games.snake.GameSettings;
import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.ui.ClientsConnectionListener;

public class Server {
	
	private ServerSocket server;
	private ExecutorService executor;
	private List<Client> clients = new ArrayList<Client>();
	
	private int numClients;
	private ClientsConnectionListener listener;
	
	public Server(int port, int numClients, ClientsConnectionListener listener) throws IOException
	{
		// this.port = port;
		this.server = new ServerSocket(port);
		this.executor = Executors.newFixedThreadPool(numClients);
		
		this.numClients = numClients;
		this.listener = listener;
	}
	
	public void waitClients(GameSettings game)
	{
		while (this.clients.size() < this.numClients) try
		{
			Client client = new Client(this.server.accept());
			client.setSnake(game.createSnake(Snake.class));
			
			this.clients.add(client);
			this.listener.clientConnected();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		// TODO Ask to start and hide window
		
		for (Client client : this.clients)
			this.executor.submit(client);
		
		game.start();
	}
	
	public void sendFrame(Snake[] snakes)
	{
		Command frame = new Command(Type.FRAME, snakes);
		
		for (Client client : this.clients) try {
			client.sendFrame(frame);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
