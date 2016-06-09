package com.forbait.games.snake.client;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.forbait.games.snake.Program;
import com.forbait.games.snake.elements.Element;
import com.forbait.games.util.Dimension;

public class ClientGame implements KeyListener, WindowListener {

	private JFrame frame;
	private ClientWorld world;
	private ClientPlayer player;
	
	public ClientGame(Dimension tiles)
	{		
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
	
	public void setPlayer(ClientPlayer player) {
		this.player = player;
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
		this.frame.dispose();
		Program.get().setWindowVisibility(true);
		this.frame.setVisible(false);
	}
	
	@Override
	public void keyPressed(KeyEvent event)
	{
		System.out.println("ClientG.keyPressed: " + event.getKeyCode());
		if (this.player != null)
			this.player.keyPressed(event.getKeyCode());
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
