package com.forbait.games.snake;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.forbait.games.snake.ui.CreatePanel;
import com.forbait.games.snake.ui.StartPanel;

public class Program implements ItemListener {

	JPanel cards; //a panel that uses CardLayout
	final static String STARTPANEL = "start";
	final static String CREATEPANEL = "create";
	 
	public void addComponentToPane(Container pane)
	{	 
		//Create the "cards".
		JPanel card1 = new StartPanel();
		JPanel card2 = new CreatePanel();
		
		//Create the panel that contains the "cards".
		cards = new JPanel(new CardLayout());
		cards.add(card1, STARTPANEL);
		cards.add(card2, CREATEPANEL);
		
		pane.add(cards, BorderLayout.CENTER);
	}
	 
	public void itemStateChanged(ItemEvent evt)
	{
		CardLayout cl = (CardLayout) cards.getLayout();
		cl.show(cards, (String) evt.getItem());
	}
	 
	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("CardLayoutDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		//Create and set up the content pane.
		Program prog = new Program();
		prog.addComponentToPane(frame.getContentPane());
		 
		//Display the window.
		frame.pack();
		frame.setVisible(true);
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
