package com.forbait.games.snake.ui;

public interface ClientsConnectionListener {

	public void setHostAddress(String address);
	public void setClientsCounter(int numPlayers, int numPlayersConnected);
	public void clientConnected();
	
}
