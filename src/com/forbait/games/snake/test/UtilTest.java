package com.forbait.games.snake.test;

import com.forbait.games.util.Dimension;
import com.forbait.games.util.Point;

public class UtilTest {

	public static void main(String[] args)
	{
		System.out.println("Testing dimension checker: " + Test.booleanAnswer(dimensionChecker())); 
	}
	
	public static boolean dimensionChecker()
	{
		Dimension d = new Dimension(10, 10);
		
		for (int x = 0; x < 10; x++)
			for (int y = 0; y < 10; y++)
				if ( ! d.contains(new Point(x, y)))
					return false;
		
		for (int y = -1; y <= 10; y++)
			if (d.contains(new Point(-1, y)) || d.contains(new Point(10, y)))
				return false;
		
		for (int x = -1; x <= 10; x++)
			if (d.contains(new Point(x, -1)) || d.contains(new Point(x, 10)))
				return false;
		
		return true;
	}
	
}
