package com.forbait.games.snake.matchserver;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MatchInfo implements Serializable {
	
	public String ip;
	public int port;
	public String name;
	public long time;
	public int numPlayers;
	public int dimension;
	public int playersLeft;
	
	public MatchInfo(
			@JsonProperty("ip") String ip,
			@JsonProperty("port") int port,
			@JsonProperty("name") String name,
			@JsonProperty("time") long time,
			@JsonProperty("numPlayers") int numPlayers,
			@JsonProperty("dimension") int dimension,
			@JsonProperty("playersLeft") int playersLeft
		) {
		this.ip = ip;
		this.port = port;
		this.name = name;
		this.time = time;
		this.numPlayers = numPlayers;
		this.dimension = dimension;
		this.playersLeft = playersLeft;
	}

	public MatchInfo(String ip, int port, String name) {
		this(ip, port, name, System.currentTimeMillis(), 0, 0, 0);
	}
	
	@Override
	public String toString() {
		return "MatchInfo " +
				"{ ip: " + this.ip +
				", port: " + this.port +
				", name: " + this.name +
				", time: " + this.time +
				", numPlayers: " + this.numPlayers +
				", dimension: " + this.dimension +
				", playersLeft: " + this.playersLeft + " }";
	}
	
}
