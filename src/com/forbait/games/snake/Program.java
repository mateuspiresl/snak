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
import com.forbait.games.snake.client.ClientManager;
import com.forbait.games.snake.server.HostGame;
import com.forbait.games.snake.server.HostManager;
import com.forbait.games.snake.ui.ConnectPanel;
import com.forbait.games.snake.ui.CreatePanel;
import com.forbait.games.snake.ui.Dialog;
import com.forbait.games.snake.ui.StartPanel;
import com.forbait.games.snake.ui.WaitDialog;
import com.forbait.games.util.Dimension;

import io.orchestrate.client.Client;
import io.orchestrate.client.OrchestrateClient;

/*
 * Janela principal.
 * Contém os métodos para criar e conectar a jogos.
 */
public class Program implements ActionListener {
	
	private static Program INSTANCE = null;
	
	public static String MATCH_SERVER_ADDRESS = "127.0.0.1";
	public static final int MATCH_SERVER_PORT = 8001;
	public static int HOST_PORT = 8000;
	
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
			changePanel(PANEL_CREATE);
			break;
			
		case StartPanel.BUTTON_CONNECT:
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
	
	/*	Create a game.
	 * 	Starts server if number of players is greater then one. */
	public void createGame(int numPlayers, int numBots, int dimension, final String hostName)
	{
		final Dimension tiles = new Dimension(dimension, dimension);
		final HostGame game = new HostGame(tiles, numBots);
		
		if (numPlayers > 1) try
		{
			final WaitDialog dialog = new WaitDialog();
			final HostManager server = new HostManager(HOST_PORT, numPlayers - 1);
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					server.notifyMatchServer(hostName);
					server.waitClients(game, tiles, dialog);
				}
			}).start();
			
			if (dialog.getAnswer())
			{
				Debug.log("Program.createG: Starting multiplayer game");
				server.start();
				game.start(server);
			}
			else
			{
				Debug.log("Program.createG: Multiplayer game creation canceled");
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
			
			JOptionPane.showConfirmDialog(this.frame,
					"Could not start a server.",
					"Error", JOptionPane.DEFAULT_OPTION
				);
		}
		else
		{
			Debug.log("Program.createG: Starting single player game");
			game.start(null);
			this.frame.setVisible(false);
		}
	}
	
	public void createGame(int numPlayers, int numBots, int dimension) {
		createGame(numPlayers, numBots, dimension, null);
	}
	
	/*	Connect to a game. */
	public void connectGame(String host, int port)
	{
		Debug.log("Program.connectG: Connect to " + host + " at " + port);
		
		try {
			new Thread(new ClientManager(host, port)).start();
		}
		catch (UnknownHostException uhe) {
			Dialog.message("Error", "Host " + host + " was not found!");
		}
		catch (IOException ioe) {
			Dialog.message("Error", "Could not connect. Check your internet connection.");
		}
	}
	
	public static void main(String[] args)
	{
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException | IllegalAccessException
				| InstantiationException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		
		// UIManager.put("swing.boldMetal", Boolean.FALSE);
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Program.get();
			}
		});
	}
	
}
