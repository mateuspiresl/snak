package com.forbait.games.snake.server;

import java.awt.BorderLayout;
import java.awt.Canvas;
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
import javax.swing.JOptionPane;
import javax.swing.Timer;

import com.forbait.games.snake.Program;
import com.forbait.games.snake.SnakeColors;
import com.forbait.games.snake.elements.Bot;
import com.forbait.games.snake.elements.Egg;
import com.forbait.games.snake.elements.Element;
import com.forbait.games.snake.elements.Movement;
import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.ui.MessagePanel;
import com.forbait.games.util.Dimension;
import com.forbait.games.util.Point;

public class HostGame implements KeyListener, ActionListener, WindowListener {

	private final int FPS = 1000 / 8;

	private Server server;
	private JFrame frame;
	private HostWorld world;
	private Timer loop;
	
	private List<Bot> bots = new ArrayList<Bot>();
	private Snake player;
	
	public HostGame(Dimension tiles, int numBots)
	{
		this.world = new HostWorld(tiles); 
		setFrame(this.world);
		
		this.player = createSnake(Snake.class);
		
		// Adds bots
		while (numBots-- > 0)
			this.bots.add(createSnake(Bot.class));
	}
	
	private void setFrame(Canvas view)
	{
		this.frame = new JFrame("Snak - Game");
		this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		this.frame.add(view);
		this.frame.addWindowListener(this);
		this.frame.addKeyListener(this);
		this.frame.setLocationRelativeTo(null);
		this.frame.pack();
	}
	
	public void start(Server server)
	{
		this.server = server;
		
		// Number of eggs is equal to the number of players
		// Starts with an extra egg
		int numEatables = this.world.countSnakes() + 1;
		for (int i = 0; i < numEatables; i++)
			this.world.add(new Egg(this.world.findEmptyCell()));
		
		this.frame.setVisible(true);
		
		// TODO Use thread here!
		this.loop = new Timer(FPS, this);
		this.loop.start();
	}
	
	public <T extends Snake> T createSnake(Class<T> type)
	{
		int numSnakes = this.world.countSnakes();
		
		int width = this.world.getTiles().width;
		int height = this.world.getTiles().height;
		
		boolean right = (numSnakes & 0x3) == 0x2 || (numSnakes & 0x3) == 0x1;
		
		int x = (int) (width * 0.1F);
		if (right) x = width - x;
		
		int y = (1 + numSnakes / 4) * (height / 5) + (right ? 1 : 0);
		if (numSnakes % 2 != 0) y = height - y;
		
		Snake snake;
		
		if (type.equals(Bot.class))
			snake = new Bot(
					SnakeColors.getColor(numSnakes),
					new Point(x, y),
					right ? Movement.LEFT : Movement.RIGHT,
					this.world
				);
		else
			snake = new Snake(
					SnakeColors.getColor(numSnakes),
					new Point(x, y),
					right ? Movement.LEFT : Movement.RIGHT
				);
		
		snake.eat();
		System.out.println("Creating " + snake);
		this.world.add(snake);
		return type.cast(snake);
	}
	
	public void close()
	{
		this.loop.stop();
		if (server != null) this.server.close();
		this.frame.dispose();
		Program.get().setWindowVisibility(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent event)
	{
		// Game over if there is no snake alive
		if (this.world.countSnakes() == 0)
		{
			System.out.println("Game.actionP: Game closing due to lack of snakes alive");
			JOptionPane.showConfirmDialog(null, "Game over!", "Snak", JOptionPane.DEFAULT_OPTION);
			close();
			return;
		}
		
		// Keep the amount of eggs
		int numEatables = this.world.countSnakes();
		for (int i = this.world.countEatables(); i < numEatables; i++)
			this.world.add(new Egg(this.world.findEmptyCell()));
		
		// Makes moves and paint
		Set<Snake> dead = this.world.move();
		
		if (this.server != null)
		{
			List<Element> elements = new ArrayList<Element>(this.world.getSnakes());
			elements.addAll(this.world.getEatables());
		
			System.out.println("HostG.actionP: Elements: " + elements);
			this.server.sendFrame(elements.toArray(new Element[0]));
			
			for (Snake snake : dead)
				this.server.notifyDeath(snake);
		}
		
		this.world.repaint();
		
		// Turn off keyboard listener if player's snake is dead
		if (dead.contains(this.player))
		{
			this.frame.removeKeyListener(this);
			
			if (this.world.countSnakes() > 0)
				new Thread(new Runnable() {
					@Override
					public void run()
					{
						JOptionPane.showConfirmDialog(null,
								new MessagePanel()
								.add("Your snake is dead! :(")
								.add("But allow the players to finish! :)"),
								"Dead", JOptionPane.DEFAULT_OPTION
							);
					}
				}).start();
		}
		
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
		System.out.println("HostG.keyPressed: Key " + event.getKeyCode());
		
		switch (event.getKeyCode())
		{
		case KeyEvent.VK_UP:
			System.out.println("HostG.keyPressed: Movement: " + Movement.UP);
			this.player.setNextMovement(Movement.UP);
			break;
			
		case KeyEvent.VK_DOWN:
			System.out.println("HostG.keyPressed: Movement: " + Movement.DOWN);
			this.player.setNextMovement(Movement.DOWN);
			break;
			
		case KeyEvent.VK_LEFT:
			System.out.println("HostG.keyPressed: Movement: " + Movement.LEFT);
			this.player.setNextMovement(Movement.LEFT);
			break;
			
		case KeyEvent.VK_RIGHT:
			System.out.println("HostG.keyPressed: Movement: " + Movement.RIGHT);
			this.player.setNextMovement(Movement.RIGHT);
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
