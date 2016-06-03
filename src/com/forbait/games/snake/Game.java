package com.forbait.games.snake;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import com.forbait.games.util.Point;

public class Game {

	private JFrame window;
	private World world;

	public Game(int numPlayers, int width, int height)
	{
		this.world = new World(width, height);
		
		this.world.add(new Snake(1, Color.BLACK, new Point(this.world.getHorizontalTiles() / 2, this.world.getVerticalTiles() / 2)));
		
		this.window = new JFrame("Snak");
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.setLayout(new BorderLayout());
		this.window.add(this.world);
		this.window.pack();
		this.window.setLocationRelativeTo(null);
		this.window.setVisible(true);
	}

}
