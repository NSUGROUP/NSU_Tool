package com.Nullvalue.NSU_Tool;

import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

public class Main
{
    private static JFrame window;
    
    public static interface User32 extends Library
    {
        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);        
        boolean SystemParametersInfo(int one, int two, String s, int three);        
    }
    
    public static void main(String[] args)
    {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
    	int monitorWidth = gd.getDisplayMode().getWidth();
        int monitorHeight = gd.getDisplayMode().getHeight();
        int windowWidth = 350;
        int windowHeight = 200;
        
    	window = new JFrame("NSU Tool");
    	JPanel mainPanel = new JPanel();
    	JButton bgButton = new JButton("Change Background");

    	window.setSize(windowWidth, windowHeight);
    	window.setLocation(((monitorWidth / 2) - windowWidth / 2), ((monitorHeight / 2) - windowHeight / 2));
    	window.getContentPane().add(mainPanel);
    	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	mainPanel.add(bgButton);
    	
		bgButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				changeBackground();
			}
		});
    	
    	window.setVisible(true);
    }
    
    public static void changeBackground()
    {
    	JFileChooser imageSelector = new JFileChooser();
        int returnValue = imageSelector.showOpenDialog(window);
        String imagePath;
        
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            imagePath = imageSelector.getSelectedFile().getPath();
            
            User32.INSTANCE.SystemParametersInfo(0x0014, 0, imagePath, 1);
            System.out.println(String.format("Changed desktop wallpaper to %s", imagePath));
        }
    }

    public static void openExe()
    {       
        Frame mainFrame = new Frame("Pick Frame");
        mainFrame.setSize(0, 0);
        String ExePath = null;
        
        JFileChooser exeFile = new JFileChooser();
        int returnValue = exeFile.showOpenDialog(mainFrame);
        
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            if (exeFile.getSelectedFile().canExecute() == true)
            {
                ExePath = exeFile.getSelectedFile().getPath();
                
                try
                {
                    Runtime.getRuntime().exec(ExePath);
                    System.out.println(String.format("Attempted to open program at path %s", ExePath));
                    
                } catch (IOException openFileException) {
                    new ErrorMessage(openFileException, "Error occured while opening file...");
                }
            } else {
                System.out.println(String.format("Failed to open file %s", ExePath));
            }
        }
    }
}
