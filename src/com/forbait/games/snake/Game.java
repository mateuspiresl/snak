package com.forbait.games.snake;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;

import com.forbait.games.snake.server.Server;

public class Game {

	private JFrame window;
	
	public Game()
	{
		World.set(40, 40, 4);
		try {
			Server.set(4);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error starting server.");
			System.exit(1);
		}
		
		this.window = new JFrame("SnakeWar");
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.setLayout(new BorderLayout());
		this.window.add(World.get());
		this.window.pack();
		this.window.setLocationRelativeTo(null);
		this.window.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new Game();
	}

}
