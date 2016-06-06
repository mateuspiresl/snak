package com.forbait.games.snake;

import com.forbait.games.snake.elements.Snake;

public interface GameSettings {

	public void start();
	public <T extends Snake> T createSnake(Class<T> type);
	
}
