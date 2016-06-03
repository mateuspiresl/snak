package com.forbait.games.snake;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.forbait.games.util.Point;

public class Game extends JFrame implements KeyListener {

	private World world;
	private List<Snake> snakes = new ArrayList<>();

	public Game(int numPlayers, int width, int height)
	{
		super("Snak - Game");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLayout(new BorderLayout());
		
		this.world = new World(width, height);	
		this.world.add(new Snake(1, Color.BLACK, new Point(this.world.getHorizontalTiles() / 2, this.world.getVerticalTiles() / 2)));
		
		super.add(this.world);
		super.addKeyListener(this);
		
		super.pack();
		super.setLocationRelativeTo(null);
		super.setVisible(true);	
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		switch (event.getKeyCode())
		{
		case KeyEvent.VK_UP:
			break;
			
		case KeyEvent.VK_DOWN:
			break;
			
		case KeyEvent.VK_LEFT:
			break;
			
		case KeyEvent.VK_RIGHT:			
		}
	}

	@Override
	public void keyReleased(KeyEvent event) { }

	@Override
	public void keyTyped(KeyEvent event) { }

}
