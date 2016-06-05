package com.forbait.games.snake.exceptions;

@SuppressWarnings("serial")
public class OccupiedCellException extends RuntimeException {

	public OccupiedCellException() { }
	
	public OccupiedCellException(String log) {
		super(log);
	}
	
	public OccupiedCellException(Exception e) {
		super(e);
	}
	
}
