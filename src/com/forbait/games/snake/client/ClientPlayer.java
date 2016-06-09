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
		System.out.println("ClientP.keyPressed: " + keyCode);
		
		switch (keyCode)
		{
		case KeyEvent.VK_UP:
			System.out.println("ClientP.keyPressed: Movement: " + Movement.UP);
			this.client.sendMovement(Movement.UP);
			break;
			
		case KeyEvent.VK_DOWN:
			System.out.println("ClientP.keyPressed: Movement: " + Movement.DOWN);
			this.client.sendMovement(Movement.DOWN);
			break;
			
		case KeyEvent.VK_LEFT:
			System.out.println("ClientP.keyPressed: Movement: " + Movement.LEFT);
			this.client.sendMovement(Movement.LEFT);
			break;
			
		case KeyEvent.VK_RIGHT:			
			System.out.println("ClientP.keyPressed: Movement: " + Movement.RIGHT);
			this.client.sendMovement(Movement.RIGHT);
		}
	}

}
