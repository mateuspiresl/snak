package com.forbait.games.snake;

import java.io.Serializable;

public class Command implements Serializable {

	private static final long serialVersionUID = -8100516669497331198L;
	
	public static Command START = new Command(Type.START);
	public static Command DEAD = new Command(Type.DEAD);
	public static Command END = new Command(Type.END);
	
	public Type type;
	public Serializable data;
	
	public Command(Type type, Serializable data) {
		this.type = type;
		this.data = data;
	}
	
	public Command(Type type) {
		this(type, null);
	}
	
	@Override
	public String toString() {
		return "Command { type: " + this.type + ", data: " + this.data + " }";
	}
	
	public static enum Type implements Serializable {
		
		// Server-Client
		DIMENSION,	// Game tiles dimension
		SNAKE,		// Client's snake
		// PLAYERS,	// Number of players connecteed
		START,		// Game start signal
		FRAME,		// Snakes bodies and colors
		// SCORE,		// Players scores changing
		DEAD,		// Client's snake is dead
		ERROR,		// Error happened, connection close
		END,		// Game end, connection close
		
		// Client-Server
		MOVEMENT,	// Movement
		
		//Both
		;
	}
	
}
