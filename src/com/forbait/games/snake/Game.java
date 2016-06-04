package com.forbait.games.snake;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.elements.Snake.Movement;
import com.forbait.games.snake.exceptions.InvalidMovementException;
import com.forbait.games.util.Point;

public class Game extends JFrame implements KeyListener, ActionListener {

	private final int FPS = 1000 / 20;
	
	private Snake localPlayer;
	private World world;
	private Map<Snake, Movement> moves = new HashMap<Snake, Movement>();
	private Timer loop;

	public Game(int numPlayers, int width, int height, Snake localPlayer)
	{
		this(numPlayers, width, height);
		
		System.out.println("Creating " + localPlayer);
		this.localPlayer = localPlayer;
		this.world.add(localPlayer);
		super.addKeyListener(this);
		
		this.world.add(new Snake(Color.BLUE, new Point(150, 150)));
	}
	
	public Game(int numPlayers, int width, int height)
	{
		super("Snak - Game");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLayout(new BorderLayout());
		
		this.world = new World(width, height);	
		
		super.add(this.world);
		super.pack();
		super.setLocationRelativeTo(null);
		super.setVisible(true);
		
		this.loop = new Timer(FPS, this);
		this.loop.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent event)
	{
		for (Snake snake : this.world.getSnakes())
		{
			Movement movement = this.moves.get(snake);
			if (movement == null)
			{
				movement = snake.getMovement();
				this.moves.put(snake, movement);
			}
			
			System.out.println("Move: " + snake.getMovement());
			
			try {
				this.world.move(snake, movement);
			} catch (InvalidMovementException e) {
				try {
					movement = movement.opposit();
					this.moves.put(snake, movement);
					this.world.move(snake, movement);
				} catch (InvalidMovementException e1) { e1.printStackTrace(); }
			}
			
//			if ( ! this.moves.get(snake).equals(snake.getMovement()))	
//			snake.move();
		}
		
		super.repaint();
	}
	
	@Override
	public void keyPressed(KeyEvent event)
	{
		switch (event.getKeyCode())
		{
		case KeyEvent.VK_UP:
			this.moves.put(this.localPlayer, Movement.UP);
			break;
			
		case KeyEvent.VK_DOWN:
			this.moves.put(this.localPlayer, Movement.DOWN);
			break;
			
		case KeyEvent.VK_LEFT:
			this.moves.put(this.localPlayer, Movement.LEFT);
			break;
			
		case KeyEvent.VK_RIGHT:			
			this.moves.put(this.localPlayer, Movement.RIGHT);
		}
	}

	@Override
	public void keyReleased(KeyEvent event) { }

	@Override
	public void keyTyped(KeyEvent event) { }

}
