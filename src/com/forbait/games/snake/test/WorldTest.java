package com.forbait.games.snake.test;

import com.forbait.games.snake.elements.Egg;
import com.forbait.games.snake.exceptions.FullWorldException;
import com.forbait.games.snake.exceptions.OccupiedCellException;
import com.forbait.games.snake.server.HostWorld;
import com.forbait.games.util.Dimension;
import com.forbait.games.util.Point;

public class WorldTest {

	public static void main(String[] args)
	{
		System.out.println("Testing empty cell finder: " + Test.booleanAnswer(emptyCellFinder()));
	}
	
	public static boolean emptyCellFinder()
	{
		HostWorld w = new HostWorld(new Dimension(30, 30));
		Point p = null;
		
		for (int i = 0; i < 30 * 30; i++)
		{
			try {
				p = w.findEmptyCell();
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
