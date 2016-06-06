package com.forbait.games.snake;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.forbait.games.snake.elements.Bot;
import com.forbait.games.snake.elements.Egg;
import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.elements.Snake.Movement;
import com.forbait.games.snake.elements.SnakeColors;
import com.forbait.games.snake.exceptions.FullWorldException;
import com.forbait.games.util.Dimension;
import com.forbait.games.util.Point;

public class Game implements KeyListener, ActionListener, WindowListener {

	private final int FPS = 1000 / 8;

	private JFrame frame;
	private Snake localPlayer;
	private World world;
	private Timer loop;
	private int numPlayers;
	
	// private int numEatables;
	private List<Bot> bots = new ArrayList<Bot>();
	
	public Game(int numPlayers, Dimension tiles, Snake localPlayer)
	{
		this(numPlayers, tiles);
		
		if (localPlayer == null)
			localPlayer = createSnake(Snake.class);
		
		localPlayer.eat();
		
		System.out.println("Creating " + localPlayer);
		this.localPlayer = localPlayer;
		this.world.add(localPlayer);
		this.frame.addKeyListener(this);
		
		// Adds bots
		for (int i = 1; i < numPlayers; i++)
		{
			Bot bot = (Bot) createSnake(Bot.class);
			this.world.add(bot);
			this.bots.add(bot);
		}
		
		// Number of eggs is equal to the number of players
		// Starts with an extra egg
		int numEatables = numPlayers + 1;
		for (int i = 0; i < numEatables; i++)
			this.world.add(new Egg(this.world.findEmptyCell()));
	}
	
	public Game(int numPlayers, Dimension tiles)
	{
		this.frame = new JFrame("Snak - Game");
		this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		
		this.numPlayers = numPlayers;
		this.world = new World(tiles);	
		
		this.frame.add(this.world);
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.addWindowListener(this);
		this.frame.setVisible(true);
		
		// TODO Use threads here!
		this.loop = new Timer(FPS, this);
		this.loop.start();
	}
	
	public <T extends Snake> T createSnake(Class<T> type)
	{
		int numSnakes = this.world.countSnakes(); 
		
		if (numSnakes == numPlayers)
			throw new FullWorldException("The number of players reached the maximum (" + this.numPlayers + ")");
		
		int width = this.world.getTiles().width;
		int height = this.world.getTiles().height;
		
		boolean right = (numSnakes & 0x3) == 0x2 || (numSnakes & 0x3) == 0x1;
		
		System.out.println(numSnakes + " " + (numSnakes & 0x3) + " " + right);
		
		int x = (int) (width * 0.1F);
		if (right) x = width - x;
		
		int y = (1 + numSnakes / 4) * (height / 5) + (right ? 1 : 0);
		if (numSnakes % 2 != 0) y = height - y;
		
		if (type.equals(Bot.class))
		{
			Bot bot = new Bot(
					SnakeColors.getColor(numSnakes),
					new Point(x, y),
					right ? Movement.LEFT : Movement.RIGHT,
					this.world
				);
			bot.eat();
			return type.cast(bot);
		}
		else
		{
			Snake snake = new Snake(
					SnakeColors.getColor(numSnakes),
					new Point(x, y),
					right ? Movement.LEFT : Movement.RIGHT
				);
			return type.cast(snake);
		}
	}
	
	public void close()
	{
		this.loop.stop();
		this.frame.dispose();
		Program.get().setWindowVisibility(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent event)
	{
		// Game oer if there is no snake alive
		if (this.world.countSnakes() == 0)
			close();
		
		// Keep the amount of eggs
		int numEatables = this.world.countSnakes();
		for (int i = this.world.countEatables(); i < numEatables; i++)
			this.world.add(new Egg(this.world.findEmptyCell()));
		
		// Makes moves and paint
		Set<Snake> dead = this.world.move();
		this.world.repaint();
		
		// Turn off keyboard listener if player's snake is dead
		if (dead.contains(this.localPlayer))
			this.frame.removeKeyListener(this);
		
		// Remove dead bots and genereate their next movements
		Iterator<Bot> botIt = this.bots.iterator();
		while (botIt.hasNext())
		{
			Bot bot = botIt.next();
			if (dead.contains(bot))
				botIt.remove();
			else
				bot.generateNextMovement();
		}
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
