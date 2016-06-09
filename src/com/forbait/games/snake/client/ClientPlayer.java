package com.forbait.games.snake.client;

import java.awt.event.KeyEvent;

import com.forbait.games.snake.elements.Movement;

public class ClientPlayer {

	private Client client;
	
	public ClientPlayer(Client client) {
		this.client = client;
	}
	
	public void keyPressed(int keyCode)
	{
		switch (keyCode)
		{
		case KeyEvent.VK_UP:
			this.client.sendMovement(Movement.UP);
			break;
			
		case KeyEvent.VK_DOWN:
			this.client.sendMovement(Movement.DOWN);
			break;
			
		case KeyEvent.VK_LEFT:
			this.client.sendMovement(Movement.LEFT);
			break;
			
		case KeyEvent.VK_RIGHT:			
			this.client.sendMovement(Movement.RIGHT);
		}
	}

}
