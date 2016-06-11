package com.forbait.games.snake.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.forbait.games.snake.Command;
import com.forbait.games.snake.Debug;
import com.forbait.games.snake.Program;
import com.forbait.games.snake.matchserver.MatchInfo;

@SuppressWarnings("serial")
public class ConnectPanel extends JPanel implements ActionListener {
	
	public static final String ACTION_CONNECT = "connect_connect";
	public static final String ACTION_UPDATE = "connect_update";
	public static final String ACTION_BACK = "connect_back";
	
	private JComboBox<String> hostsList;
	private JButton connectButton, updateButton, backButton;
	private Map<String, MatchInfo> hosts;
	
	public ConnectPanel() {
		super(new BorderLayout());
//		super.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
//		super.setLayout(new FlowLayout());
		
		this.hostsList = new JComboBox<String>(new String[0]);
		this.hostsList.addActionListener(this);
		updateList();
				
		this.connectButton = new JButton("Connect");
		this.connectButton.setActionCommand(ACTION_CONNECT);
		this.connectButton.addActionListener(this);
		
		this.updateButton = new JButton("Update");
		this.updateButton.setActionCommand(ACTION_UPDATE);
		this.updateButton.addActionListener(this);
		
		this.backButton = new JButton("Cancel");
		this.backButton.setActionCommand(ACTION_BACK);
		this.backButton.addActionListener(Program.get());
		
		// Insertion
		
		JPanel block;
		
		block = new JPanel();
		block.add(this.hostsList);
		super.add(block, BorderLayout.CENTER);
		
		block = new JPanel(new FlowLayout());
		block.add(this.backButton);
		block.add(this.updateButton);
		block.add(this.connectButton);
		super.add(block, BorderLayout.SOUTH);
	}
	
	private void updateList()
	{
		Socket socket = null;
		try {
			socket = new Socket(Program.MATCH_SERVER_ADDRESS, Program.MATCH_SERVER_PORT);
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new Command(Command.Type.CLIENT));
//			oos.close();
			
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			this.hosts = (Map<String, MatchInfo>) ois.readObject();
		}
		catch (IOException e) {
			e.printStackTrace();
			this.hosts = Collections.emptyMap();
		}
		catch (ClassNotFoundException e) {
			Debug.log("ConnectP.updateL: Wrong answer from server");
			e.printStackTrace();
			this.hosts = Collections.emptyMap();
		}
		finally {
			if (socket != null) try {
				socket.close();
			} catch (IOException e) { e.printStackTrace(); }			
		}
		
		this.hostsList.setModel(new DefaultComboBoxModel<String>(
				this.hosts.keySet().toArray(new String[0])
			));
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
		case ACTION_CONNECT:
			MatchInfo info = this.hosts.get(this.hostsList.getSelectedItem());
			if (info == null)
				info = new MatchInfo("127.0.0.1", Program.HOST_PORT, "");
			
			Program.get().connectGame(info.ip, info.port);
			break;
			
		case ACTION_UPDATE:
			updateList();
		}
	}

}
