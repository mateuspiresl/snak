package com.forbait.games.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ServerInfo implements Serializable {
	
	public String ip;
	public int port;
	public String name;
	
	public ServerInfo(String ip, int port, String name) {
		this.ip = ip;
		this.port = port;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return ip + " " + port + " " + name;
	}
	
}
