package com.forbait.games.snake.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class WaitDialog implements ClientsConnectionListener, ActionListener {

	private static final String BUTTON_START = "Start";
	private static final String BUTTON_CANCEL = "Cancel";
	
	private JOptionPane pane = null;
	private JButton startButton;
	private JLabel label;
	
	private String host = "unknown";
	private int numPlayers;
	private int numPlayersConnected;
	
	public WaitDialog() {
		this.label = new JLabel();
	}
	
	public boolean getAnswer()
	{
		this.startButton = new JButton(BUTTON_START);
		this.startButton.setEnabled(false);
		this.startButton.setActionCommand(BUTTON_START);
		this.startButton.addActionListener(this);
		
		JButton cancelButton = new JButton(BUTTON_CANCEL);
		cancelButton.setActionCommand(BUTTON_CANCEL);
		cancelButton.addActionListener(this);
		
		updateText();
		
		return JOptionPane.showOptionDialog(
				null, 
				label, 
				"Waiting for players", 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, 
				null, 
				new Object[] { this.startButton, cancelButton }, 
				this.startButton
			) == 1;
	}

	private void updateText() { 
		this.label.setText("Your IP: " + this.host
				+ "\nPlayers connected: " + this.numPlayersConnected
				+ " of " + this.numPlayers
			);
	}
	
	@Override
	public void setHostAddress(String address) {
		this.host = address;
		updateText();
	}

	@Override
	public void setClientsCounter(int numPlayers, int numPlayersConnected)
	{
		this.numPlayers = numPlayers;
		this.numPlayersConnected = numPlayersConnected;
		updateText();
	}

	@Override
	public void clientConnected()
	{
		this.numPlayersConnected++;
		updateText();
		
		if (this.numPlayersConnected == this.numPlayers)
			this.startButton.setEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent event)
	{
		if (this.pane == null)
			this.pane = getOptionPane((JComponent) event.getSource());
		
		switch (event.getActionCommand())
		{
		case BUTTON_START:
			this.pane.setValue(true);
			break;
			
		case BUTTON_CANCEL:
			this.pane.setValue(false);
		}
	}
	
	private static JOptionPane getOptionPane(JComponent parent)
	{
		JOptionPane pane = null;
		
		if (!(parent instanceof JOptionPane))
			pane = getOptionPane((JComponent) parent.getParent());
		else
			pane = (JOptionPane) parent;

		return pane;
	}
	
}
