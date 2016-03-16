package com.forbait.games.snake;

import java.io.Serializable;

public class Command implements Serializable {

	// Generated serial version
	private static final long serialVersionUID = 3389421930549723376L;
	
	public Type type;
	public Object data;
	
	public Command(Type type, Object data) {
		this.type = type;
		this.data = data;
	}
	
	public static enum Type {
		MOVEMENT;
	}
	
}
