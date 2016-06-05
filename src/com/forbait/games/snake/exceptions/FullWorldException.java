package com.forbait.games.snake.exceptions;

@SuppressWarnings("serial")
public class FullWorldException extends RuntimeException {

	public FullWorldException() { }
	
	public FullWorldException(String log) {
		super(log);
	}
	
	public FullWorldException(Exception e) {
		super(e);
	}
	
}
