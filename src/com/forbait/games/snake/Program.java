package com.forbait.games.snake;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.forbait.games.snake.Command.Type;
import com.forbait.games.snake.server.Server;
import com.forbait.games.snake.ui.ClientsConnectionListener;
import com.forbait.games.snake.ui.CreatePanel;
import com.forbait.games.snake.ui.StartPanel;
import com.forbait.games.snake.ui.WaitDialog;
import com.forbait.games.snake.ui.WaitingClientsPanel;
import com.forbait.games.util.Dimension;

public class Program implements ActionListener { //ItemListener {
	
	private static Program INSTANCE = null;
	public final static String PANEL_START = "start";
	public final static String PANEL_CREATE = "create";
	public final static String PANEL_WAITING = "waiting";
	
	private JFrame frame;
	private JPanel cards;
	private ClientsConnectionListener connectionListener;
	
	private Program()
	{
		this.frame = new JFrame("Snak");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Program.INSTANCE = this;
		
		WaitingClientsPanel clientsPanel = new WaitingClientsPanel();
		this.connectionListener = clientsPanel;
		
		this.cards = new JPanel(new CardLayout());
		this.cards.add(new StartPanel(), PANEL_START);
		this.cards.add(new CreatePanel(), PANEL_CREATE);
		this.cards.add(clientsPanel, PANEL_WAITING);
		
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
	
	public int[] openCreateGameDiaglog()
	{
		final JOptionPane optionPane = new JOptionPane(
				"The only way to close this dialog is by\n"
				+ "pressing one of the following buttons.\n"
				+ "Do you understand?",
				JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_OPTION);

		final JDialog dialog = new JDialog(
				frame, 
				"Click a button",
				true
			);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(
			JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				// return null
			}
		});
		optionPane.addPropertyChangeListener(
			new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					String prop = e.getPropertyName();
		
					if (dialog.isVisible() 
							&& (e.getSource() == optionPane)
							&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
						//If you were going to check something
						//before closing the window, you'd do
						//it here.
						dialog.setVisible(false);
					}
				}
			});
		dialog.pack();
		dialog.setVisible(true);
		
		int value = ((Integer) optionPane.getValue()).intValue();
		if (value == JOptionPane.YES_OPTION) {
			// return data
		} else if (value == JOptionPane.NO_OPTION) {
			// return null
		}
		
		return null;
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
			
		case CreatePanel.ACTION_BACK:
		default:
			changePanel(PANEL_START);
			break;
		}
	}
	
	public void createGame(int numPlayers, int numBots, int dimension)
	{
		final Game game = new Game(numPlayers, new Dimension(dimension, dimension), numBots);
		
		if (numPlayers > 1) try
		{
			this.connectionListener.setClientsCounter(numPlayers, 1);
			changePanel(PANEL_WAITING);
			
			final WaitDialog dialog = new WaitDialog();
			final Server server = new Server(8001, numPlayers - 1);
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					server.waitClients(game, dialog);
				}
			}).start();
			
			if (dialog.getAnswer())
			{
				server.start();
				game.start();
			}
			else
			{
				server.sendCommand(new Command(Type.END));
				server.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			
			// TODO
		}
		else
		{
			System.out.println(numPlayers + " " + numBots + " " + dimension);
			game.start();
			this.frame.setVisible(false);
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
