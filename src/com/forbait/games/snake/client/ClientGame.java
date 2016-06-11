package com.forbait.games.snake.client;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.forbait.games.snake.Debug;
import com.forbait.games.snake.Program;
import com.forbait.games.snake.elements.Element;
import com.forbait.games.snake.elements.Movement;
import com.forbait.games.util.Dimension;

public class ClientGame implements KeyListener, WindowListener {

	private JFrame frame;
	private ClientWorld world;
	private ClientManager client;
	
	public ClientGame(Dimension tiles, ClientManager client)
	{
		this.client = client;
		
		this.frame = new JFrame("Snak - Game");
		this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		
		this.world = new ClientWorld(tiles);
		
		this.frame.add(this.world);
		this.frame.addWindowListener(this);
		this.frame.addKeyListener(this);
		this.frame.setLocationRelativeTo(null);
		this.frame.pack();
	}
	
	public void start() {
		this.frame.setVisible(true);
		Program.get().setWindowVisibility(false);
	}
	
	public void step(Element[] elements)
	{
		this.world.setElements(elements);
		this.world.repaint();
	}
	
	public void close()
	{
		this.client.close();
		this.frame.dispose();
		Program.get().setWindowVisibility(true);
		this.frame.setVisible(false);
	}
	
	@Override
	public void keyPressed(KeyEvent event)
	{
		Debug.log("ClientG.keyPressed: " + event.getKeyCode());
		
		switch (event.getKeyCode())
		{
		case KeyEvent.VK_UP:
			Debug.log("ClientG.keyPressed: Movement: " + Movement.UP);
			this.client.sendMovement(Movement.UP);
			break;
			
		case KeyEvent.VK_DOWN:
			Debug.log("ClientG.keyPressed: Movement: " + Movement.DOWN);
			this.client.sendMovement(Movement.DOWN);
			break;
			
		case KeyEvent.VK_LEFT:
			Debug.log("ClientG.keyPressed: Movement: " + Movement.LEFT);
			this.client.sendMovement(Movement.LEFT);
			break;
			
		case KeyEvent.VK_RIGHT:			
			Debug.log("ClientG.keyPressed: Movement: " + Movement.RIGHT);
			this.client.sendMovement(Movement.RIGHT);
		}
	}
	
	@Override
	public void windowClosing(WindowEvent event) {
		close();
	}

	@Override
	public void keyReleased(KeyEvent event) { }

	@Override
	public void keyTyped(KeyEvent event) { }
	
	@Override
	public void windowActivated(WindowEvent event) { }

	@Override
	public void windowClosed(WindowEvent event) { }

	@Override
	public void windowDeactivated(WindowEvent event) { }

	@Override
	public void windowDeiconified(WindowEvent event) { }

	@Override
	public void windowIconified(WindowEvent event) { }

	@Override
	public void windowOpened(WindowEvent event) { }
	
}
