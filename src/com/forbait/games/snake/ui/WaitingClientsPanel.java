package com.forbait.games.snake.ui;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.forbait.games.snake.Program;

public class WaitingClientsPanel extends JPanel implements ClientsConnectionListener {
	
	private int numPlayers;
	private int numPlayersConnected;
	private JLabel clientsCounter; 
	private JLabel hostAddress; 
	
	public WaitingClientsPanel() {
		super();
		super.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		String ip = "desconhecido";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) { e.printStackTrace(); }
		
		this.clientsCounter = new JLabel();
		updateCounter();
		
		this.hostAddress = new JLabel();
		setHostAddress("desconhecido.");
		
		super.add(this.hostAddress);
		super.add(this.clientsCounter);
	}
	
	private void updateCounter() {
		this.clientsCounter.setText("Jogadores conectados: " + this.numPlayersConnected + " / " + this.numPlayers);
	}
	
	@Override
	public void setClientsCounter(int numPlayers, int numPlayersConnected)
	{
		this.numPlayers = numPlayers;
		this.numPlayersConnected = numPlayersConnected;
		updateCounter();
	}
	
	@Override
	public void clientConnected()
	{
		this.numPlayersConnected++;
		updateCounter();
	}

	@Override
	public void setHostAddress(String address) {
		this.hostAddress.setText("Seu IP é " + address);
	}

}
