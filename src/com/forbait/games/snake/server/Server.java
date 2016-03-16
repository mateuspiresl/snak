package com.forbait.games.snake.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.forbait.games.snake.Snake;
import com.forbait.games.snake.World;
import com.forbait.games.util.ImmutablePoint;
import com.forbait.games.util.RandomColor;

public class Server {

	public static Server instance;
	private static final int PORT = 8080;
	
	private ServerSocket server;
	private ExecutorService executor;
	private List<Client> clients;
	
	private int numClients;
	private World world;
	
	private Server(int numClients, World world) throws IOException
	{
		this.world = world;
		this.server = new ServerSocket(PORT);
		this.numClients = numClients;
		this.executor = Executors.newFixedThreadPool(numClients);
	}
	
	public Server newServer(int numClients, World world) throws IOException
	{
		if (instance == null)
			return Server.instance = new Server(numClients, world);
		else
			return Server.instance;
	}
	
	public void waitClients()
	{
		while (clients.size() < numClients)
		{
			try {
				this.clients.add(new Client(this.server.accept(), this, this.world));
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	public void start()
	{
		Random rnd = new Random();
		RandomColor rc = new RandomColor();
		
		for (int i = 0; i < this.clients.size(); i++)
		{
			this.clients.get(i).setSnake(new Snake(i, rc.nextColor(), new ImmutablePoint(rnd.nextInt(200), rnd.nextInt(200))));
			this.executor.execute(this.clients.get(i));
		}
	}
	
	public void notifyClients(Snake snake)
	{
		//for (Client client : this.clients)
		{
			//client
		}
	}
	
}
