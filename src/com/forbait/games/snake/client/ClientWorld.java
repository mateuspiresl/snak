package com.forbait.games.snake.client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.forbait.games.snake.Debug;
import com.forbait.games.snake.elements.Element;
import com.forbait.games.util.Dimension;

@SuppressWarnings("serial")
public class ClientWorld extends Canvas {

	public static final int MULTIPLIER = 14;
	
	private Element[] elements;
	
	public ClientWorld(Dimension tiles)
	{
		super();
		super.setBackground(new Color(225, 255, 225));
		super.setSize(new java.awt.Dimension(tiles.width * MULTIPLIER, tiles.height * MULTIPLIER));
	}
	
	public void setElements(Element[] elements)
	{
		Debug.log("ClientW.setE: Updating elements to draw: " + elements);
		this.elements = elements;
	}
	
	@Override
	public void paint(Graphics graphics)
	{
		Graphics2D g = (Graphics2D) graphics;

		Debug.log("ClientW.paint");
		
		for (Element element : this.elements)
			element.draw(g);
	}
	
}
