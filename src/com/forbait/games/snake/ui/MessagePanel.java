package com.forbait.games.snake.ui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MessagePanel extends JPanel {

	public MessagePanel() {
		super();
		super.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
	
	public MessagePanel add(String message) {
		super.add(new JLabel(message));
		return this;
	}
	
}
