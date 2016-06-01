package com.forbait.games.snake.ui;

import java.awt.FlowLayout;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

public class CreatePanel extends JPanel {
	
	public CreatePanel() {
		super();
		
		FlowLayout container = new FlowLayout();
		
		FlowLayout upperBlock = new FlowLayout();
		
		JLabel numPlayersLabel = new JLabel("Número de jogadores:");
		super.add(numPlayersLabel);
		
		JFormattedTextField numPlayersField = null;
		try {
			numPlayersField = new JFormattedTextField(new MaskFormatter("##"));
		} catch (ParseException e) { e.printStackTrace(); }
		numPlayersField.setColumns(2);
		super.add(numPlayersField);
		
		JLabel sizeLabel = new JLabel("Tamanho:");
		super.add(sizeLabel);
		
		JFormattedTextField widthField = null;
		try {
			widthField = new JFormattedTextField(new MaskFormatter("###"));
		} catch (ParseException e) { e.printStackTrace(); }
		widthField.setColumns(2);
		super.add(widthField);
		
		JFormattedTextField heightField = null;
		try {
			heightField = new JFormattedTextField(new MaskFormatter("###"));
		} catch (ParseException e) { e.printStackTrace(); }
		heightField.setColumns(2);
		super.add(heightField);
		
		/*JButton button = new JButton("Criar");
		button.setActionCommand(BUTTON_NEW);
		button.addActionListener(buttonListener);
		super.add(button);*/
		// TODO
	}

}
