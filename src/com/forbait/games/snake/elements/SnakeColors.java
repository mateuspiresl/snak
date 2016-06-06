package com.forbait.games.snake.elements;

import java.awt.Color;

public class SnakeColors {
	
	private static String[] colors = {
		"#637a91", // dark gray
		"#e15258", // red
		"#39add1", // light blue
//		"#51b46d", // green
		"#e0ab18", // mustard
		"#f9845b", // orange
		"#838cc7", // lavender
		"#c25975", // mauve
		"#7d669e", // purple
		"#3079ab", // dark blue
//		"#f092b0", // pink
		"#53bbb4", // aqua
//		"#b7c0c7"  // light gray
	};
	
	private int index = 0;
	
	public SnakeColors reset() {
		this.index = 0;
		return this;
	}
	
	public Color next()
	{
		if (index == colors.length)
			index = 0;
		
		return Color.decode(colors[index++]);
	}
	
	public static Color getColor(int index) {
		return Color.decode(colors[index % colors.length]);
	}
	
}
