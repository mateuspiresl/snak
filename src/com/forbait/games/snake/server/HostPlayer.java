package com.forbait.games.snake.server;

import java.awt.event.KeyEvent;

import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.elements.Snake.Movement;

public class HostPlayer {
	
	private Snake snake;
	
	public HostPlayer(Snake snake) {
		this.snake = snake;
	}
	
	public Snake getSnake() {
		return this.snake;
	}
	
	public void keyPressed(int keyCode)
	{
		switch (keyCode)
		{
		case KeyEvent.VK_UP:
			this.snake.setNextMovement(Movement.UP);
			break;
			
		case KeyEvent.VK_DOWN:
			this.snake.setNextMovement(Movement.DOWN);
			break;
			
		case KeyEvent.VK_LEFT:
			this.snake.setNextMovement(Movement.LEFT);
			break;
			
		case KeyEvent.VK_RIGHT:			
			this.snake.setNextMovement(Movement.RIGHT);
		}
	}
	
}
