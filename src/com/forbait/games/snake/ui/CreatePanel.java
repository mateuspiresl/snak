package com.forbait.games.snake.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import com.forbait.games.snake.Program;

public class CreatePanel extends JPanel implements ActionListener {
	
	private Program program;
	private JTextField	numPlayersField,
						widthField,
						heightField;
	
	public CreatePanel() {
		super();
		super.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		numPlayersField = new JTextField();
		((PlainDocument) numPlayersField.getDocument()).setDocumentFilter(new NumbersFilter(2));
		numPlayersField.setColumns(2);
		numPlayersField.addActionListener(this);
		numPlayersField.setText("4");
		
		widthField = new JTextField();
		((PlainDocument) widthField.getDocument()).setDocumentFilter(new NumbersFilter(3));
		widthField.setColumns(3);
		widthField.addActionListener(this);
		widthField.setText("30");
		
		heightField = new JTextField();
		((PlainDocument) heightField.getDocument()).setDocumentFilter(new NumbersFilter(3));
		heightField.setColumns(3);
		heightField.addActionListener(this);
		heightField.setText("30");
		
		JButton button = new JButton("Criar");
		button.addActionListener(this);
		
		// Insertion
		
		JPanel topBlock = new JPanel(new FlowLayout());
		topBlock.add(new JLabel("Número de jogadores:"));
		topBlock.add(numPlayersField);
		
		JPanel middleBlock = new JPanel(new FlowLayout());
		middleBlock.add(new JLabel("Tamanho:"));
		middleBlock.add(widthField);
		middleBlock.add(new JLabel("x"));
		middleBlock.add(heightField);
		
		JPanel bottomBlock = new JPanel(new FlowLayout());
		bottomBlock.add(button);
		
		super.add(topBlock);
		super.add(middleBlock);
		super.add(bottomBlock);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Program.get().createGame(
				Integer.parseInt(this.numPlayersField.getText()),
				Integer.parseInt(this.widthField.getText()),
				Integer.parseInt(this.heightField.getText())
			);
	}
	
	private class NumbersFilter extends DocumentFilter {
	
		private final int length; 
		
		public NumbersFilter(int length) {
			this.length = length;
		}
		
		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
		{
			Document doc = fb.getDocument();
			if (doc.getLength() == this.length) return;
			
			if (text.length() > 2 - doc.getLength())
				text = text.substring(0, this.length - doc.getLength());
			
			if (text.matches("\\d+"))
				super.replace(fb, offset, length, text, attrs);
		}
		
	}

}
