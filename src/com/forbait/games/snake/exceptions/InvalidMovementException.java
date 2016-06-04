package com.forbait.games.snake.exceptions;

@SuppressWarnings("serial")
public class InvalidMovementException extends Exception {

	public InvalidMovementException() { }
	
	public InvalidMovementException(String log) {
		super(log);
	}
	
	public InvalidMovementException(Exception e) {
		super(e);
	}
	
}
