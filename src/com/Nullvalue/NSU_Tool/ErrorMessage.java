package com.Nullvalue.NSU_Tool;

import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ErrorMessage
{
	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    
	int monitorWidth = gd.getDisplayMode().getWidth();
    int monitorHeight = gd.getDisplayMode().getHeight();
    
    public void showMessage(String message)
    {
    	JFrame errorWindow = new JFrame();
		
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
		Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
		int textWidth = 0;
		
		for (int i = 0; i < message.split("\n").length; i++)
			if ((int) (font.getStringBounds(message.split("\n")[i], frc).getWidth()) > textWidth)
				textWidth = (int) (font.getStringBounds(message.split("\n")[i], frc).getWidth());
		
		int windowWidthPadding = 60;
		int windowHeightPadding = 100;
		int windowWidth = textWidth + windowWidthPadding;
		int windowHeight = (message.split("\n").length * 15) + windowHeightPadding;
		
		errorWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		errorWindow.setSize(windowWidth, windowHeight);
		errorWindow.setLocation(((monitorWidth / 2) - windowWidth / 2), ((monitorHeight / 2) - windowHeight / 2));
		
		JLabel labelMessage = new JLabel("<HTML>" + message.replace("\n", "<br>") + "</HTML>", JLabel.CENTER);
		labelMessage.setFont(font);
		errorWindow.add(labelMessage);
		
		errorWindow.setVisible(true);
    }
    
	public ErrorMessage(String message)
	{
		showMessage(message);
	}
	
	public ErrorMessage(Exception e, String Precursor)
	{
		StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String message = sw.toString();

        showMessage("<b>" + Precursor + "</b>" + "\n" + message);
	}
	
	public ErrorMessage(Exception e)
	{
		StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String message = sw.toString();

        showMessage(message);
	}
}
