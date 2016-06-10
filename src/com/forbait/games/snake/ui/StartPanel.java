package com.forbait.games.snake.ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.forbait.games.snake.Program;

@SuppressWarnings("serial")
public class StartPanel extends JPanel {

	public static final String BUTTON_NEW = "New Game";
	public static final String BUTTON_CONNECT = "Connect";
	public static final String BUTTON_EXIT = "Exit";
	
	public StartPanel() {
		super();
		super.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JButton button;
		
		super.add(Box.createVerticalStrut(6));
		button = new JButton(BUTTON_NEW);
		button.setAlignmentX(0.5F);
		button.setActionCommand(BUTTON_NEW);
		button.addActionListener(Program.get());
		super.add(button);
		
		super.add(Box.createVerticalStrut(6));
		button = new JButton(BUTTON_CONNECT);
		button.setAlignmentX(0.5F);
		button.setActionCommand(BUTTON_CONNECT);
		button.addActionListener(Program.get());
		super.add(button);
		
		super.add(Box.createVerticalStrut(6));
		button = new JButton(BUTTON_EXIT);
		button.setAlignmentX(0.5F);
		button.setActionCommand(BUTTON_EXIT);
		button.addActionListener(Program.get());
		super.add(button);
		super.add(Box.createVerticalStrut(6));
	}
	
}
