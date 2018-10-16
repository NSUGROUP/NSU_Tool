package com.Nullvalue.NSU_Tool;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.concurrent.Callable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfirmationMessage
{
	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    
	int monitorWidth = gd.getDisplayMode().getWidth();
    int monitorHeight = gd.getDisplayMode().getHeight();
	
	public ConfirmationMessage(String message)
	{
		JFrame confirmationWindow = new JFrame();
		JPanel messagePanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JButton confirmationButton = new JButton("Okay");
		
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
		Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
		int textWidth = 0;
		
		for (int i = 0; i < message.split("\n").length; i++)
			if ((int) (font.getStringBounds(message.split("\n")[i], frc).getWidth()) > textWidth)
				textWidth = (int) (font.getStringBounds(message.split("\n")[i], frc).getWidth());
		
		int windowWidthPadding = 60;
		int windowHeightPadding = 100;
		int windowWidth = textWidth + windowWidthPadding;
		int windowHeight = (message.split("\n").length * 15) + windowHeightPadding + confirmationButton.getHeight();
		
		messagePanel.setSize(windowWidth, (message.split("\n").length * 15) + (windowHeightPadding / 2));
		buttonPanel.setSize(windowWidth, confirmationButton.getHeight() + (windowHeightPadding / 2));
		
		confirmationWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		confirmationWindow.setSize(windowWidth, windowHeight);
		confirmationWindow.setResizable(false);
		confirmationWindow.add(messagePanel, BorderLayout.NORTH);
		confirmationWindow.add(buttonPanel, BorderLayout.SOUTH);
		confirmationWindow.setLocation(((monitorWidth / 2) - windowWidth / 2), ((monitorHeight / 2) - windowHeight / 2));
		confirmationWindow.setIconImage(new ImageIcon(Main.class.getResource("/images/blank.png")).getImage());
		
		JLabel labelMessage = new JLabel("<HTML>" + message.replace("\n", "<br>") + "</HTML>", JLabel.CENTER);
		labelMessage.setFont(font);
		
		messagePanel.add(labelMessage);
		buttonPanel.add(confirmationButton);
		
		confirmationButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				confirmationWindow.dispose();
			}
		});
		
		confirmationWindow.setVisible(true);
	}
	
	public <T> ConfirmationMessage(String message, Callable<T> yesFunc)
	{
		JFrame confirmationWindow = new JFrame();
		JPanel messagePanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JButton yesButton = new JButton("Yes");
		JButton noButton = new JButton("No");
		
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
		Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
		int textWidth = 0;
		
		for (int i = 0; i < message.split("\n").length; i++)
			if ((int) (font.getStringBounds(message.split("\n")[i], frc).getWidth()) > textWidth)
				textWidth = (int) (font.getStringBounds(message.split("\n")[i], frc).getWidth());
		
		int windowWidthPadding = 60;
		int windowHeightPadding = 100;
		int windowWidth = textWidth + windowWidthPadding;
		int windowHeight = (message.split("\n").length * 15) + windowHeightPadding + yesButton.getHeight();
		
		messagePanel.setSize(windowWidth, (message.split("\n").length * 15) + (windowHeightPadding / 2));
		buttonPanel.setSize(windowWidth, yesButton.getHeight() + (windowHeightPadding / 2));
		
		confirmationWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		confirmationWindow.setSize(windowWidth, windowHeight);
		confirmationWindow.add(messagePanel, BorderLayout.NORTH);
		confirmationWindow.add(buttonPanel, BorderLayout.SOUTH);
		confirmationWindow.setLocation(((monitorWidth / 2) - windowWidth / 2), ((monitorHeight / 2) - windowHeight / 2));
		confirmationWindow.setIconImage(new ImageIcon(Main.class.getResource("/images/blank.png")).getImage());
		
		JLabel labelMessage = new JLabel("<HTML>" + message.replace("\n", "<br>") + "</HTML>", JLabel.CENTER);
		labelMessage.setFont(font);
		
		messagePanel.add(labelMessage);
		buttonPanel.add(yesButton);
		buttonPanel.add(noButton);
		
		yesButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					yesFunc.call();
					confirmationWindow.dispose();
				} catch (Exception e1)
				{
					e1.printStackTrace();
					confirmationWindow.dispose();
				}
			}
		});
		
		noButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					confirmationWindow.dispose();
				} catch (Exception e1)
				{
					e1.printStackTrace();
					confirmationWindow.dispose();
				}
			}
		});
		
		confirmationWindow.setVisible(true);
	}
	
	public <T> ConfirmationMessage(String message, Callable<T> yesFunc, Callable<T> noFunc)
	{
		JFrame confirmationWindow = new JFrame();
		JPanel messagePanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JButton yesButton = new JButton("Yes");
		JButton noButton = new JButton("No");
		
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
		Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
		int textWidth = 0;
		
		for (int i = 0; i < message.split("\n").length; i++)
			if ((int) (font.getStringBounds(message.split("\n")[i], frc).getWidth()) > textWidth)
				textWidth = (int) (font.getStringBounds(message.split("\n")[i], frc).getWidth());
		
		int windowWidthPadding = 60;
		int windowHeightPadding = 100;
		int windowWidth = textWidth + windowWidthPadding;
		int windowHeight = (message.split("\n").length * 15) + windowHeightPadding + yesButton.getHeight();
		
		messagePanel.setSize(windowWidth, (message.split("\n").length * 15) + (windowHeightPadding / 2));
		buttonPanel.setSize(windowWidth, yesButton.getHeight() + (windowHeightPadding / 2));
		
		confirmationWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		confirmationWindow.setSize(windowWidth, windowHeight);
		confirmationWindow.add(messagePanel, BorderLayout.NORTH);
		confirmationWindow.add(buttonPanel, BorderLayout.SOUTH);
		confirmationWindow.setLocation(((monitorWidth / 2) - windowWidth / 2), ((monitorHeight / 2) - windowHeight / 2));
		confirmationWindow.setIconImage(new ImageIcon(Main.class.getResource("/images/blank.png")).getImage());
		
		JLabel labelMessage = new JLabel("<HTML>" + message.replace("\n", "<br>") + "</HTML>", JLabel.CENTER);
		labelMessage.setFont(font);
		
		messagePanel.add(labelMessage);
		buttonPanel.add(yesButton);
		buttonPanel.add(noButton);
		
		yesButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					yesFunc.call();
					confirmationWindow.dispose();
				} catch (Exception e1)
				{
					e1.printStackTrace();
					confirmationWindow.dispose();
				}
			}
		});
		
		noButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					noFunc.call();
					confirmationWindow.dispose();
				} catch (Exception e1)
				{
					e1.printStackTrace();
					confirmationWindow.dispose();
				}
			}
		});
		
		confirmationWindow.setVisible(true);
	}
}
