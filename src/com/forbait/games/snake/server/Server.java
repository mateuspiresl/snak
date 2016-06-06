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

import com.forbait.games.snake.World;
import com.forbait.games.snake.elements.SnakeColors;
import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.ui.ClientsConnectionListener;
import com.forbait.games.util.Point;

public class Server
{
	// private final int port;
	
	private ServerSocket server;
	private ExecutorService executor;
	private List<Future<?>> tasks = new ArrayList<Future<?>>();
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
	
	public void waitClients()
	{
		while (clients.size() < numClients)
		{
			try {
				this.clients.add(new Client(this.server.accept()));
				this.listener.clientConnected();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	/*public void start()
	{
		Random rnd = new Random();
		RandomColor rc = new RandomColor();
		
		for (int i = 0; i < this.clients.size(); i++)
		{
			this.clients.get(i).setSnake(new Snake(
					i,
					rc.next(),
					new Point(rnd.nextInt(World.get().getWidth()), rnd.nextInt(World.get().getHeight()))
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
	public static class UnsetServerException extends RuntimeException { }*/
	
}
