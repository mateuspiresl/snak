package com.forbait.games.snake.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.forbait.games.snake.DB;
import com.forbait.games.snake.Program;
import com.forbait.games.snake.server.GameInfo;
import io.orchestrate.client.ResponseListener;
import io.orchestrate.client.Result;
import io.orchestrate.client.SearchResults;

@SuppressWarnings("serial")
public class ConnectPanel extends JPanel implements ActionListener, ResponseListener<SearchResults<GameInfo>> {
	
	public static final String ACTION_CONNECT = "connect_connect";
	public static final String ACTION_UPDATE = "connect_update";
	public static final String ACTION_BACK = "connect_back";
	
	private JComboBox<String> hostsList;
	private JButton connectButton, updateButton, backButton;
	private Map<String, GameInfo> hosts;
	
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

	private static String generateReadableInfo(GameInfo info) {
		return info.name + ": "
				+ info.dimension + "x" + info.dimension + ", "
				+ info.numPlayers + "/" + info.numPlayersLeft + " players, "
				+ info.numBots + " bots";
	}

	private void updateList()
	{
		SearchResults<GameInfo> results = DB.blockingGetGames();
		this.hosts = new HashMap<String, GameInfo>();

		for (Result<GameInfo> result : results)
		{
			GameInfo info = result.getKvObject().getValue();
			this.hosts.put(generateReadableInfo(info), info);
		}
		
		this.hostsList.setModel(new DefaultComboBoxModel<>(
				this.hosts.keySet().toArray(new String[0])
			));
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
		case ACTION_CONNECT:
			GameInfo info = this.hosts.get(this.hostsList.getSelectedItem());
			if (info == null)
				info = new GameInfo("127.0.0.1", Program.HOST_PORT, "");
			
			Program.get().connectGame(info.ip, info.port);
			break;
			
		case ACTION_UPDATE:
			updateList();
		}
	}

	@Override
	public void onFailure(Throwable throwable) {

	}

	@Override
	public void onSuccess(SearchResults<GameInfo> results) {

	}

}
