package com.forbait.games.snake;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.forbait.games.snake.elements.Snake;
import com.forbait.games.snake.server.Server;
import com.forbait.games.snake.ui.ClientsConnectionListener;
import com.forbait.games.snake.ui.CreatePanel;
import com.forbait.games.snake.ui.StartPanel;
import com.forbait.games.snake.ui.WaitingClientsPanel;

public class Program extends JFrame implements ActionListener { //ItemListener {

	private static Program INSTANCE = null;
	public final static String PANEL_START = "start";
	public final static String PANEL_CREATE = "create";
	public final static String PANEL_WAITING = "waiting";
	
	private JPanel cards;
	private ClientsConnectionListener connectionListener;
	
	private Program()
	{
		super("Snak");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Program.INSTANCE = this;
		
		WaitingClientsPanel clientsPanel = new WaitingClientsPanel();
		this.connectionListener = clientsPanel;
		
		this.cards = new JPanel(new CardLayout());
		this.cards.add(new StartPanel(), PANEL_START);
		this.cards.add(new CreatePanel(), PANEL_CREATE);
		this.cards.add(clientsPanel, PANEL_WAITING);
		
		super.add(this.cards, BorderLayout.CENTER);
		super.pack();
		super.setVisible(true);
	}
	
	public static Program get()
	{
		if (Program.INSTANCE == null) new Program();
		return Program.INSTANCE;
	}
	 
	public void changePanel(String panelName)
	{
		CardLayout cl = (CardLayout) this.cards.getLayout();
		cl.show(this.cards, panelName);
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
			break;
			
		case StartPanel.BUTTON_EXIT:
			break;
			
		case CreatePanel.BUTTON_BACK:
		default:
			changePanel(PANEL_START);
			break;
		}
	}
	
	public void createGame(int numPlayers, int width, int height)
	{
		if (numPlayers > 1) try
		{
			this.connectionListener.setClientsCounter(numPlayers, 1);
			changePanel(PANEL_WAITING);
			
			Server server = new Server(8001, numPlayers - 1, this.connectionListener);
			
			new Thread(new Runnable() {
				private Server server;
				
				public Runnable setServer(Server server) {
					this.server = server;
					return this;
				}
				
				@Override
				public void run() {
					server.waitClients();
				}
			}.setServer(server)).start();
		}
		catch (IOException e) {
			e.printStackTrace();
			
			// TODO
		}
		else
		{
			new Game(numPlayers, width, height, null);
			super.setVisible(false);
		}
	}
	
	public static void main(String[] args)
	{
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException | IllegalAccessException
				| InstantiationException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		
		// Turn off metal's use of bold fonts
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		 
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Program.get();
			}
		});
	}
	
}
