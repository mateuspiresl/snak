package com.forbait.games.snake.matchserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.forbait.games.snake.Debug;
import com.forbait.games.snake.Program;

public class MatchServer {

	private ServerSocket socket;
	private Map<String, MatchInfo> hosts = new HashMap<String, MatchInfo>();
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	public MatchServer()
	{
		try {
			this.socket = new ServerSocket(Program.MATCH_SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start()
	{
		Debug.log("MatchS.start");
		while (true)
		{
			try {
				executor.submit(new MatchHost(this, this.socket.accept()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void addMatch(MatchInfo info, String name) {
		Debug.log("MatchS.addM: Adding " + name + " at " + info.ip);
		this.hosts.put(name, info);
	}
	
	public synchronized void remove(String name) {
		Debug.log("MatchS.addM: Removing " + name);
		this.hosts.remove(name);
	}
	
	public synchronized Map<String, MatchInfo> getHosts() {
		Debug.log("MatchS.addM: Getting " + this.hosts.size() + " hosts");
		return this.hosts;
	}
	
	public static void main(String[] args)
	{
		MatchServer server = new MatchServer();
		server.start();
	}
	
}
