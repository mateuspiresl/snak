package com.forbait.games.snake;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.BindException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.forbait.games.snake.Command.Type;
import com.forbait.games.snake.client.Client;
import com.forbait.games.snake.server.HostGame;
import com.forbait.games.snake.server.Server;
import com.forbait.games.snake.ui.ConnectPanel;
import com.forbait.games.snake.ui.CreatePanel;
import com.forbait.games.snake.ui.StartPanel;
import com.forbait.games.snake.ui.WaitDialog;
import com.forbait.games.util.Dimension;

public class Program implements ActionListener { //ItemListener {
	
	private static Program INSTANCE = null;
	public final static String PANEL_START = "start";
	public final static String PANEL_CREATE = "create";
	private static final String PANEL_CONNECT = "connect";
	
	private JFrame frame;
	private JPanel cards;
	
	private Program()
	{
		this.frame = new JFrame("Snak");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Program.INSTANCE = this;
		
		this.cards = new JPanel(new CardLayout());
		this.cards.add(new StartPanel(), PANEL_START);
		this.cards.add(new CreatePanel(), PANEL_CREATE);
		this.cards.add(new ConnectPanel(), PANEL_CONNECT);
		
		this.frame.add(this.cards, BorderLayout.CENTER);
		this.frame.pack();
		this.frame.setVisible(true);
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
	
	public void setWindowVisibility(boolean visible) {
		this.frame.setVisible(visible);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		switch (event.getActionCommand())
		{
		case StartPanel.BUTTON_NEW:
//			createGame(2, 6, 40);
			changePanel(PANEL_CREATE);
			break;
			
		case StartPanel.BUTTON_CONNECT:
//			try {
//				new Thread(new Client("127.0.0.1", 8001)).start();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			changePanel(PANEL_CONNECT);
			break;
			
		case StartPanel.BUTTON_EXIT:
			this.frame.dispose();
			this.frame.setVisible(false);
			break;
			
		case CreatePanel.ACTION_BACK:
		default:
			changePanel(PANEL_START);
			break;
		}
	}
	
	public void createGame(int numPlayers, int numBots, int dimension)
	{
		final Dimension tiles = new Dimension(dimension, dimension);
		final HostGame game = new HostGame(tiles, numBots);
		
		if (numPlayers > 1) try
		{
			final WaitDialog dialog = new WaitDialog();
			final Server server = new Server(8000, numPlayers - 1);
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					server.waitClients(game, tiles, dialog);
				}
			}).start();
			
			if (dialog.getAnswer())
			{
				server.start();
				game.start(server);
			}
			else
			{
				server.sendCommand(new Command(Type.END));
				server.close();
			}
		}
		catch (BindException be) {
			JOptionPane.showConfirmDialog(this.frame,
					"Port 8000 in use by another application.",
					"Error", JOptionPane.DEFAULT_OPTION
				);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			
			// TODO
		}
		else
		{
			System.out.println(numPlayers + " " + numBots + " " + dimension);
			game.start(null);
			this.frame.setVisible(false);
		}
	}
	
	public void connectGame(String host)
	{
		System.out.println("Program.connectG: host: " + host);
		
		try {
			new Thread(new Client(host, 8000)).start();
		}
		catch (UnknownHostException uhe) {
			JOptionPane.showConfirmDialog(this.frame,
					"Host " + host + " was not found!", "Error", JOptionPane.DEFAULT_OPTION);
		}
		catch (IOException ioe) {
			JOptionPane.showConfirmDialog(this.frame,
					"Could not connect. Check your internet connection.", "Error", JOptionPane.DEFAULT_OPTION);
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
