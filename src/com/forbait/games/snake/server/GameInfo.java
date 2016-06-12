package com.forbait.games.snake.server;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GameInfo implements Serializable {
	
	public final String ip;
	public final int port;
	public final String name;
	public final int dimension;
	public final int numPlayers;
	public final int numBots;
	public int numPlayersLeft;
	public long time;

	public GameInfo(
			@JsonProperty("ip") String ip,
			@JsonProperty("port") int port,
			@JsonProperty("name") String name,
			@JsonProperty("dimension") int dimension,
			@JsonProperty("numPlayers") int numPlayers,
			@JsonProperty("numBots") int numBots,
			@JsonProperty("numPlayersLeft") int playersLeft,
			@JsonProperty("time") long time
	) throws NullPointerException {
		if (ip == null) throw new NullPointerException();

		this.ip = ip;
		this.port = port;
		this.name = name;
		this.time = time;
		this.dimension = dimension;
		this.numPlayers = numPlayers;
		this.numBots = numBots;
		this.numPlayersLeft = playersLeft;
	}

	public GameInfo(String ip, int port, String name, int dimension, int numPlayers, int numBots, int playersLeft) {
		this(ip, port, name, dimension, numPlayers, numBots, playersLeft, System.currentTimeMillis());
	}

	public GameInfo(String ip, int port, String name) {
		this(ip, port, name, 0, 0, 0, 0, System.currentTimeMillis());
	}

	public void setNumPlayersLeft(int num) {
		this.numPlayersLeft = num;
	}
	public void countPlayer() { this.numPlayersLeft++; }
	public void setTime(long time) { this.time = time; }
	public void updateTime() { setTime(System.currentTimeMillis()); }

	@Override
	public String toString() {
		return "GameInfo " +
				"{ ip: " + this.ip +
				", port: " + this.port +
				", name: " + this.name +
				", time: " + this.time +
				", numPlayers: " + this.numPlayers +
				", numBots: " + this.numBots +
				", dimension: " + this.dimension +
				", numPlayersLeft: " + this.numPlayersLeft + " }";
	}
}
