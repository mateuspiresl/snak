package com.forbait.games.snake.ui;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.forbait.games.snake.Program;

public class WaitingClientsPanel extends JPanel {
	
	private Program program;
	
	public WaitingClientsPanel(Program program) {
		super();
		super.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		String ip = "desconhecido";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) { e.printStackTrace(); }
		
		super.add(new JLabel("Seu IP é: " + ip));
	}

}
