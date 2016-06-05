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

import com.forbait.games.snake.elements.Egg;
import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.elements.Snake.Movement;
import com.forbait.games.util.Dimension;
import com.forbait.games.util.Point;

public class Game implements KeyListener, ActionListener {

	private final int FPS = 1000 / 8;

	private JFrame frame;
	private Snake localPlayer;
	private World world;
	private Timer loop;
	
	private int numEatables;

	public Game(int numPlayers, Dimension tiles, Snake localPlayer)
	{
		this(numPlayers, tiles);
		
		if (localPlayer == null)
			localPlayer = createRandomSnake(Color.BLACK);
		
		localPlayer.eat();
		
		System.out.println("Creating " + localPlayer);
		this.localPlayer = localPlayer;
		this.world.add(localPlayer);
		this.frame.addKeyListener(this);
		
		Snake enemy = new Snake(Color.BLUE, new Point(15, 15), Movement.DOWN);
		enemy.eat();
		this.world.add(enemy);
		System.out.println("Creating " + enemy);
		
		numEatables = numPlayers / 2;
		if (numEatables == 0) numEatables = 1;
		
		for (int i = 0; i < numEatables; i++)
			this.world.add(new Egg(this.world.findEmptyCell()));
		
		// Extra egg
		this.world.add(new Egg(this.world.findEmptyCell()));
	}
	
	public Game(int numPlayers, Dimension tiles)
	{
		this.frame = new JFrame("Snak - Game");
		this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		
		this.world = new World(tiles);	
		
		this.frame.add(this.world);
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
		
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
		
		int width = this.world.getTiles().width;
		int height = this.world.getTiles().height;
		
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
			this.frame.dispose();
			// this.frame.setVisible(false);
			Program.get().setWindowVisibility(true);
		}
		
		for (int i = this.world.countEatables(); i < numEatables; i++)
			this.world.add(new Egg(this.world.findEmptyCell()));
		
		Set<Snake> dead = this.world.move();
		
		if (dead.contains(this.localPlayer))
			this.frame.removeKeyListener(this);
		
		this.frame.repaint();
	}
	
	@Override
	public void keyPressed(KeyEvent event)
	{
		switch (event.getKeyCode())
		{
		case KeyEvent.VK_UP:
			this.localPlayer.setNextMovement(Movement.UP);
			break;
			
		case KeyEvent.VK_DOWN:
			this.localPlayer.setNextMovement(Movement.DOWN);
			break;
			
		case KeyEvent.VK_LEFT:
			this.localPlayer.setNextMovement(Movement.LEFT);
			break;
			
		case KeyEvent.VK_RIGHT:			
			this.localPlayer.setNextMovement(Movement.RIGHT);
		}
	}

	@Override
	public void keyReleased(KeyEvent event) { }

	@Override
	public void keyTyped(KeyEvent event) { }

}
