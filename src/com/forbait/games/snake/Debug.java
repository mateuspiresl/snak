package com.forbait.games.snake;

public class Debug {

	public static boolean active = true;
	
	public static void log(String str) {
		if (active) System.out.println(str);
	}
	
}
