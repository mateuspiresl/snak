package com.forbait.games.snake.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.forbait.games.snake.Program;

@SuppressWarnings("serial")
public class ConnectPanel extends JPanel implements ActionListener {
	
	public static final String ACTION_CONNECT = "connect_connect";
	public static final String ACTION_BACK = "connect_back";
	
	private JTextField hostText;
	private JButton connectButton, backButton;
	
	public ConnectPanel() {
		super(new BorderLayout());
//		super.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
//		super.setLayout(new FlowLayout());
		
		this.hostText = new JTextField();
		this.hostText.setToolTipText("Ex.: 192.168.0.21");
		this.hostText.setColumns(10);
				
		this.connectButton = new JButton("Connect");
		this.connectButton.setActionCommand(ACTION_CONNECT);
		this.connectButton.addActionListener(this);
		
		this.backButton = new JButton("Cancel");
		this.backButton.setActionCommand(ACTION_BACK);
		this.backButton.addActionListener(Program.get());
		
		// Insertion
		
		JPanel block;
		
		block = new JPanel();
		block.add(new JLabel("Host IP:"));
		block.add(this.hostText);
		super.add(block, BorderLayout.CENTER);
		
		block = new JPanel(new FlowLayout());
		block.add(this.backButton);
		block.add(this.connectButton);
		super.add(block, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(ACTION_CONNECT))
		{
			this.connectButton.setEnabled(false);
			this.backButton.setEnabled(false);
			
			Program.get().connectGame(this.hostText.getText());
		}
	}

}
