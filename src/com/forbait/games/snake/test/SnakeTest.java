package com.forbait.games.snake.test;

import com.forbait.games.snake.Snake;
import com.forbait.games.snake.World;
import com.forbait.games.util.ImmutablePoint;
import com.forbait.games.util.RandomColor;

public class SnakeTest {

	public static void main(String[] args)
	{
		World.set(40, 40);
		
		Snake s = new Snake(0, new RandomColor().next(), new ImmutablePoint(40, 40));
		s.eat(new ImmutablePoint(41, 40));
		s.eat(new ImmutablePoint(42, 40));
		
		print(s);
		
		System.out.println("Breaking! Size: " + s.breakAt(2).length);
		print(s);
		
		System.out.println("Breaking! Size: " + s.breakAt(new ImmutablePoint(41, 40)).length);
		print(s);
		
		System.out.println("Breaking! Size: " + s.breakAt(0).length);
		print(s);
	}
	
	public static void print(Snake s)
	{
		System.out.println("Parts:");
		for (ImmutablePoint part : s.getBody())
			System.out.println(part.getX() + " " + part.getY());
		System.out.println("---");
	}
	
}
