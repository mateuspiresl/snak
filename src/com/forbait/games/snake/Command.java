package com.forbait.games.snake;

import java.io.Serializable;

public class Command implements Serializable {

	// Generated serial version
	private static final long serialVersionUID = 3389421930549723376L;
	
	public Type type;
	public Serializable data;
	
	public Command(Type type, Serializable data) {
		this.type = type;
		this.data = data;
	}
	
	public Command(Type type) {
		this(type, null);
	}
	
	public static enum Type {
		MOVEMENT, SNAKE, FRAME, START, ERROR, END;
	}
	
}
