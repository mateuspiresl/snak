package com.forbait.games.snake.ui;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class StartPanel extends JPanel {

	public static final String BUTTON_NEW = "Novo";
	public static final String BUTTON_CONNECT = "Conectar";
	public static final String BUTTON_EXIT = "Sair";
	
	public StartPanel(ActionListener buttonListener) {
		super();
		
		JButton button;
		
		button = new JButton(BUTTON_NEW);
		button.setActionCommand(BUTTON_NEW);
		button.addActionListener(buttonListener);
		super.add(button);
		
		button = new JButton(BUTTON_CONNECT);
		button.setActionCommand(BUTTON_CONNECT);
		button.addActionListener(buttonListener);
		super.add(button);
		
		button = new JButton(BUTTON_EXIT);
		button.setActionCommand(BUTTON_EXIT);
		button.addActionListener(buttonListener);
		super.add(button);
	}
	
}
