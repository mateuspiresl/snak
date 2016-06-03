package com.forbait.games.snake.ui;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.forbait.games.snake.Program;

public class StartPanel extends JPanel {

	public static final String BUTTON_NEW = "Novo";
	public static final String BUTTON_CONNECT = "Conectar";
	public static final String BUTTON_EXIT = "Sair";
	
	public StartPanel() {
		super();
		
		JButton button;
		
		button = new JButton(BUTTON_NEW);
		button.setActionCommand(BUTTON_NEW);
		button.addActionListener(Program.get());
		super.add(button);
		
		button = new JButton(BUTTON_CONNECT);
		button.setActionCommand(BUTTON_CONNECT);
		button.addActionListener(Program.get());
		super.add(button);
		
		button = new JButton(BUTTON_EXIT);
		button.setActionCommand(BUTTON_EXIT);
		button.addActionListener(Program.get());
		super.add(button);
	}
	
}
