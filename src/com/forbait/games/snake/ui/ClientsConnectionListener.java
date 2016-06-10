package com.forbait.games.snake.ui;

public interface ClientsConnectionListener {

	public void setClientsCounter(int numPlayers, int numPlayersConnected);
	public void clientConnected();
	
}
