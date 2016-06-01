package com.forbait.games.snake.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.forbait.games.snake.Snake;
import com.forbait.games.snake.World;
import com.forbait.games.util.ImmutablePoint;
import com.forbait.games.util.RandomColor;

public class Server {

	private static final Server INSTANCE = new Server();
	private static final int PORT = 8080;
	
	private ServerSocket server;
	private ExecutorService executor;
	private List<Future<?>> tasks = new ArrayList<Future<?>>();
	private List<Client> clients;
	
	private int numClients;
	
	private Server() { }
	
	public static Server get()
	{
		if (INSTANCE.server == null)
			throw new UnsetServerException();
		else
			return INSTANCE;
	}
	
	public static void set(int numClients) throws IOException
	{
		INSTANCE.server = new ServerSocket(PORT);
		INSTANCE.numClients = numClients;
		INSTANCE.executor = Executors.newFixedThreadPool(numClients);
	}
	
	public void waitClients()
	{
		while (clients.size() < numClients)
		{
			try {
				this.clients.add(new Client(this.server.accept()));
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
			this.clients.get(i).setSnake(new Snake(
					i,
					rc.next(),
					new ImmutablePoint(rnd.nextInt(World.get().getWidth()), rnd.nextInt(World.get().getHeight()))
				));
			
			this.tasks.add(this.executor.submit(this.clients.get(i)));
		}
	}
	
	public void sendSnake(Snake snake)
	{
		Iterator<Client> iterator = this.clients.iterator();
		for (int i = 0; iterator.hasNext(); i++)
		{
			Client client = iterator.next();
			
			try {
				client.sendSnake(snake);
			} catch (IOException ioe) {
				this.tasks.get(i).cancel(true);
				iterator.remove();
			}
		}
	}
	
	@SuppressWarnings("serial")
	public static class UnsetServerException extends RuntimeException { }
	
}
