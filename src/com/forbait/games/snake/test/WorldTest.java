package com.forbait.games.snake.test;

import com.forbait.games.snake.World;
import com.forbait.games.snake.elements.Egg;
import com.forbait.games.snake.exceptions.FullWorldException;
import com.forbait.games.snake.exceptions.OccupiedCellException;
import com.forbait.games.util.Dimension;
import com.forbait.games.util.Point;

public class WorldTest {

	public static void main(String[] args)
	{
		System.out.println("Testing random position generator: " + booleanAnswer(testRandomPositionGenerator()));
	}
	
	public static String booleanAnswer(boolean answer) {
		return answer ? "OK" : "Fail";
	}
	
	public static boolean testRandomPositionGenerator()
	{
		World w = new World(new Dimension(30, 30));
		Point p = null;
		
		for (int i = 0; i < 30 * 30; i++)
		{
			try {
				p = w.getEmptyPosition();
			} catch (FullWorldException fwe) {
				return false;
			}
			
			try {
				w.add(new Egg(p));
			} catch (OccupiedCellException oce) {
				System.out.println(p);
				System.out.println(w.countOccupied());
				throw oce;
			}
		}
		
		try {
			w.add(new Egg(p));
		} catch (OccupiedCellException oce) {
			return true;
		}
		
		return false;
	}
	
}
