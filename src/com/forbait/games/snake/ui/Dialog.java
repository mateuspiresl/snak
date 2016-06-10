package com.forbait.games.snake.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

public class Dialog {

	private static final ExecutorService executor = Executors.newCachedThreadPool();
	
	public static void nonBlockingMessage(final String title, final String message)
	{
		executor.submit(new Runnable() {
				@Override
				public void run() {
					JOptionPane.showConfirmDialog(null, message, title, JOptionPane.DEFAULT_OPTION);
				}
			});
	}
	
	public static void nonBlockingMessage(final String title, final MessagePanel message)
	{
		executor.submit(new Runnable() {
				@Override
				public void run() {
					JOptionPane.showConfirmDialog(null, message, title, JOptionPane.DEFAULT_OPTION);
				}
			});
	}
	
	public static void message(final String title, final String message)
	{
		JOptionPane.showConfirmDialog(null, message, title, JOptionPane.DEFAULT_OPTION);
	}
	
	public static void message(final String title, final MessagePanel message)
	{
		JOptionPane.showConfirmDialog(null, message, title, JOptionPane.DEFAULT_OPTION);
	}
	
	public static boolean confirm(final String title, final String message)
	{
		return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION)
				== JOptionPane.OK_OPTION;
	}
	
	public static boolean confirm(final String title, final MessagePanel message)
	{
		return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION)
				== JOptionPane.OK_OPTION;
	}
	
}
