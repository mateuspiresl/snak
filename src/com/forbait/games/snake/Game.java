package com.forbait.games.snake;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.elements.Snake.Movement;
import com.forbait.games.util.Point;

@SuppressWarnings("serial")
public class Game extends JFrame implements KeyListener, ActionListener {

	private final int FPS = 1000 / 8;
	
	private Snake localPlayer;
	private World world;
	private Timer loop;

	public Game(int numPlayers, int width, int height, Snake localPlayer)
	{
		this(numPlayers, width, height);
		
		if (localPlayer == null)
		{
			Random rnd = new Random();
			int quad = rnd.nextInt(4);
			
			int x = 0, y = 0;
			Snake.Movement movement = null;
			
			switch (quad)
			{
			case 0:
			case 2:	x = (int) (width * 0.06) + rnd.nextInt((int) (width * 0.60)); break;
			case 1:
			case 3: x = (int) (width * 0.4 ) + rnd.nextInt((int) (width * 0.60));
			}
			
			switch (quad)
			{
			case 0:
			case 1:	y = (int) (height * 0.06) + rnd.nextInt((int) (height * 0.60)); break;
			case 2:
			case 3: y = (int) (height * 0.34 ) + rnd.nextInt((int) (height * 0.60));
			}
			
			switch (quad)
			{
			case 0: movement = rnd.nextBoolean() ? Snake.Movement.DOWN : Snake.Movement.RIGHT; break;
			case 1:	movement = rnd.nextBoolean() ? Snake.Movement.DOWN : Snake.Movement.LEFT; break;
			case 2: movement = rnd.nextBoolean() ? Snake.Movement.UP : Snake.Movement.RIGHT; break;
			case 3: movement = rnd.nextBoolean() ? Snake.Movement.UP : Snake.Movement.LEFT; break;
			default: movement = null;
			}
			
			localPlayer = new Snake(Color.BLACK, new Point(x, y), movement);
		}
		
		localPlayer.eat();
		
		System.out.println("Creating " + localPlayer);
		this.localPlayer = localPlayer;
		this.world.add(localPlayer);
		super.addKeyListener(this);
		
		// this.world.add(new Snake(Color.BLUE, new Point(150, 150)));
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
		Set<Snake> dead = this.world.move();
		
		if (dead.contains(this.localPlayer))
			super.removeKeyListener(this);
		
		super.repaint();
	}
	
	@Override
	public void keyPressed(KeyEvent event)
	{
		switch (event.getKeyCode())
		{
		case KeyEvent.VK_UP:
			this.world.addMovement(this.localPlayer, Movement.UP);
			break;
			
		case KeyEvent.VK_DOWN:
			this.world.addMovement(this.localPlayer, Movement.DOWN);
			break;
			
		case KeyEvent.VK_LEFT:
			this.world.addMovement(this.localPlayer, Movement.LEFT);
			break;
			
		case KeyEvent.VK_RIGHT:			
			this.world.addMovement(this.localPlayer, Movement.RIGHT);
		}
	}

	@Override
	public void keyReleased(KeyEvent event) { }

	@Override
	public void keyTyped(KeyEvent event) { }

}
