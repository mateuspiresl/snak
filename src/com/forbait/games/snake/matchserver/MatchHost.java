package com.forbait.games.snake.matchserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.forbait.games.snake.Command;
import com.forbait.games.snake.Debug;

public class MatchHost implements Runnable {

	private MatchServer server;
	private Socket host;

	public MatchHost(MatchServer server, Socket host)
	{
		this.server = server;
		this.host = host;
	}
	
	@Override
	public void run()
	{
		Debug.log("MatchH.run: New");
		ObjectInputStream ois = null;
		
		try {
			ois = new ObjectInputStream(this.host.getInputStream());
			
			Command cmd = (Command) ois.readObject();
			
			if (cmd.type.equals(Command.Type.SERVER))
			{
				Debug.log("MatchH.run: Server connect");
				MatchInfo info = (MatchInfo) cmd.data;
				info.ip = this.host.getInetAddress().getHostAddress();
				
				Debug.log("MatchH.run: Server info: " + info);
				this.server.addMatch(info, info.name);
				
				while (this.host.isConnected())
				{
					Debug.log("MatchH.run: Still connected");
					try {
						super.wait(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				this.server.remove(info.name);
				Debug.log("MatchH.run: Host " + info.name + " disconnected");
			}
			else if (cmd.type.equals(Command.Type.CLIENT))
			{
				Debug.log("MatchH.run: Client connect");
				ObjectOutputStream oos = new ObjectOutputStream(this.host.getOutputStream());
				oos.writeObject(this.server.getHosts());
				oos.close();
				
				Debug.log("MatchH.run: Client disconnected");
				return;
			}
			else return;
		}
		catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		finally {
			if (ois != null) try {
				ois.close();
			} catch (IOException e) { e.printStackTrace(); }
			
			try {
				this.host.close();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
	
}
