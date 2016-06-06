package com.forbait.games.snake.test;

import java.util.Scanner;

import com.forbait.games.snake.Game;
import com.forbait.games.util.Dimension;

public class SinglePlayer {

	public static void main(String[] args)
	{
		Scanner s = new Scanner(System.in);
		int n = s.nextInt();
		int b = s.nextInt();
		int d = s.nextInt();
		new Game(n, new Dimension(d, d), b);
	}
	
}
