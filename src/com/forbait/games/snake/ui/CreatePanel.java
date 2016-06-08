package com.forbait.games.snake.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.forbait.games.snake.Program;

@SuppressWarnings("serial")
public class CreatePanel extends JPanel implements ActionListener {
	
	public static final String ACTION_CREATE = "create_create";
	public static final String ACTION_BACK = "create_back";
	
	private JComboBox<Integer>	numPlayersList,
								numBotsList,
								dimensionsList;
	private int numPlayers = 1,
				numBots = 0,
				dimension = 15;
	
	private final Integer[] PLAYERS = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8 };
	private final Integer[] BOTS = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7 };
	private final Integer[] DIMENSIONS = new Integer[] { 40, 35, 30, 25, 20, 15, 10 };
	
	public CreatePanel() {
		super(new BorderLayout());
		
		this.numPlayersList = new JComboBox<Integer>(PLAYERS);
		this.numBotsList = new JComboBox<Integer>(BOTS);
		this.dimensionsList = new JComboBox<Integer>(DIMENSIONS);
		
		this.numPlayersList.addActionListener(this);
		this.numBotsList.addActionListener(this);		
		this.dimensionsList.addActionListener(this);

		this.numBotsList.setSelectedIndex(0);
		this.numPlayersList.setSelectedIndex(2);
		this.dimensionsList.setSelectedIndex(4);
		
		JButton createButton = new JButton("Criar");
		createButton.setActionCommand(ACTION_CREATE);
		createButton.addActionListener(this);
		
		JButton backButton = new JButton("Voltar");
		backButton.setActionCommand(ACTION_BACK);
		backButton.addActionListener(Program.get());
		
		// Insertion
		
		JPanel center = new JPanel();
		JPanel block;
		
		block = new JPanel();
		block.add(new JLabel("Players:"));
		block.add(this.numPlayersList);
		center.add(block);
		
		block = new JPanel();
		block.add(new JLabel("Bots:"));
		block.add(this.numBotsList);
		center.add(block);
		
		block = new JPanel();
		block.add(new JLabel("Dimension:"));
		block.add(this.dimensionsList);
		center.add(block);
		
		super.add(center, BorderLayout.CENTER);
		
		block = new JPanel(new FlowLayout());
		block.add(backButton);
		block.add(createButton);
		super.add(block, BorderLayout.SOUTH);
	}
	
	public void setBotsList()
	{
		if (this.numBots > 8 - this.numPlayers)
			this.numBots = 8 - this.numPlayers;
		
		this.numBotsList.setModel(new DefaultComboBoxModel<Integer>(
				Arrays.copyOfRange(BOTS, 0, BOTS.length - this.numPlayers + 1)
			));
		this.numBotsList.setSelectedIndex(this.numBots);
	}
	
	public void setDimensionsList()
	{
		int minDimension;
		
		if (this.numPlayers + this.numBots >= 7)
			minDimension = 20;
		else if (this.numPlayers + this.numBots >= 4)
			minDimension = 15;
		else
			minDimension = 10;
		
		if (this.dimension < minDimension)
			this.dimension = minDimension;
		
		this.dimensionsList.setModel(new DefaultComboBoxModel<Integer>(
				Arrays.copyOfRange(DIMENSIONS, 0, DIMENSIONS.length - (minDimension - 10) / 5)
			));
		this.dimensionsList.setSelectedIndex((40 - minDimension) / 5);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(ACTION_CREATE))
		{
			Program.get().createGame(this.numPlayers, this.numBots, this.dimension);
			
			return;
		}
		else if (e.getSource().equals(this.numPlayersList))
		{
			int value = (Integer) this.numPlayersList.getSelectedItem();
			if (value == this.numPlayers) return;
			
			this.numPlayers = value;
			setBotsList();
			setDimensionsList();
		}
		else if (e.getSource().equals(this.numBotsList))
		{
			int value = (Integer) this.numBotsList.getSelectedItem();
			if (value == this.numBots) return;
			
			this.numBots = value;
			setDimensionsList();
		}
		else if (e.getSource().equals(this.dimensionsList))
		{
			this.dimension = (Integer) this.dimensionsList.getSelectedItem();
		}
	}

}
