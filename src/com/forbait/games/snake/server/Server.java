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
	
	public Server(int port, int numClients) throws IOException
	{
		this.server = new ServerSocket(port);
		this.executor = Executors.newFixedThreadPool(numClients);
		this.numClients = numClients;
	}
	
	public void waitClients(GameSettings game, ClientsConnectionListener listener)
	{
		listener.setHostAddress(this.server.getInetAddress().getHostAddress());
		listener.setClientsCounter(this.numClients, 1);
		
		while (this.clients.size() < this.numClients) try
		{
			Client client = new Client(this.server.accept());
			client.setSnake(game.createSnake(Snake.class));
			
			this.clients.add(client);
			listener.clientConnected();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			listener.clientConnected();
		}
	}
	
	public void start()
	{
		for (Client client : this.clients)
			this.executor.submit(client);
	}
	
	public void sendFrame(Snake[] snakes) {
		sendCommand(new Command(Type.FRAME, snakes));
	}
	
	public void sendCommand(Command cmd)
	{
		for (Client client : this.clients) try {
			client.sendCommand(cmd);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		for (Client client : this.clients)
			client.close();
		
		try {
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
