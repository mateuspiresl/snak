package com.forbait.games.snake;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.forbait.games.snake.ui.CreatePanel;
import com.forbait.games.snake.ui.StartPanel;

public class Program implements ActionListener { //ItemListener {

	private static final Program INSTANCE = new Program();
	
	private JPanel cards;
	public final static String PANEL_START = "start";
	public final static String PANEL_CREATE = "create";
	
	public static Program get() {
		return INSTANCE;
	}
	
	public void addComponentToPane(Container pane)
	{	 
		//Create the "cards".
		JPanel card1 = new StartPanel(this);
		JPanel card2 = new CreatePanel();
		
		//Create the panel that contains the "cards".
		cards = new JPanel(new CardLayout());
		cards.add(card1, PANEL_START);
		cards.add(card2, PANEL_CREATE);
		
		pane.add(cards, BorderLayout.CENTER);
	}
	 
	public void changePanel(String panelName)
	{
		CardLayout cl = (CardLayout) cards.getLayout();
		cl = (CardLayout) cards.getLayout();
		cl.show(cards, panelName);
	}
	 
	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("Snak");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		//Create and set up the content pane.
		Program prog = new Program();
		prog.addComponentToPane(frame.getContentPane());
		 
		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		switch (event.getActionCommand())
		{
		case StartPanel.BUTTON_NEW:
			changePanel(PANEL_CREATE);
			break;
			
		case StartPanel.BUTTON_CONNECT:
			
		case StartPanel.BUTTON_EXIT:
			
		default:
			changePanel(PANEL_START);
			break;
		}
	}
	
	public void createGame(int numPlayers, int width, int height)
	{
		new Game(numPlayers, width, height);
	}
	
	public static void main(String[] args) {
		/* Use an appropriate Look and Feel */
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException | IllegalAccessException
				| InstantiationException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		 
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
}
