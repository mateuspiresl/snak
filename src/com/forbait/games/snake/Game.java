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

@SuppressWarnings("serial")
public class Game extends JFrame implements KeyListener, ActionListener {

	private final int FPS = 1000 / 8;
	
	private Snake localPlayer;
	private World world;
	private Timer loop;

	public Game(int numPlayers, Dimension tiles, Snake localPlayer)
	{
		this(numPlayers, tiles);
		
		if (localPlayer == null)
			localPlayer = createRandomSnake(Color.BLACK);
		
		localPlayer.eat();
		
		System.out.println("Creating " + localPlayer);
		this.localPlayer = localPlayer;
		this.world.add(localPlayer);
		super.addKeyListener(this);
		
		// this.world.add(new Snake(Color.BLUE, new Point(150, 150)));
	}
	
	public Game(int numPlayers, Dimension tiles)
	{
		super("Snak - Game");
		super.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		super.setLayout(new BorderLayout());
		
		this.world = new World(tiles);	
		
		super.add(this.world);
		super.pack();
		super.setLocationRelativeTo(null);
		super.setVisible(true);
		
		// Use threads here!
		this.loop = new Timer(FPS, this);
		this.loop.start();
	}
	
	public Snake createRandomSnake(Color color)
	{
		Random rnd = new Random();
		int quad = rnd.nextInt(4);
		
		int x = 0, y = 0;
		Snake.Movement movement = null;
		
		int width = this.world.getTiles().getWidth();
		int height = this.world.getTiles().getHeight();
		
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
		
		return new Snake(Color.BLACK, new Point(x, y), movement);
	}
	
	public Snake createRandomSnake()
	{
		return null;
	}
	
	@Override
	public void actionPerformed(ActionEvent event)
	{
		if (this.world.getSnakes().isEmpty())
		{
			this.loop.stop();
			this.dispose();
			this.setVisible(false);
			Program.get().setVisible(true);
		}
		
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
			this.world.setMovement(this.localPlayer, Movement.UP);
			break;
			
		case KeyEvent.VK_DOWN:
			this.world.setMovement(this.localPlayer, Movement.DOWN);
			break;
			
		case KeyEvent.VK_LEFT:
			this.world.setMovement(this.localPlayer, Movement.LEFT);
			break;
			
		case KeyEvent.VK_RIGHT:			
			this.world.setMovement(this.localPlayer, Movement.RIGHT);
		}
	}

	@Override
	public void keyReleased(KeyEvent event) { }

	@Override
	public void keyTyped(KeyEvent event) { }

}
