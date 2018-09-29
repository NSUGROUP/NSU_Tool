package com.Nullvalue.NSU_Tool;

import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

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
        int windowWidth = 220;
        int windowHeight = 200;
        
    	window = new JFrame("NSU Tool");
    	JPanel mainPanel = new JPanel();

    	window.setSize(windowWidth, windowHeight);
    	window.setLocation(((monitorWidth / 2) - windowWidth / 2), ((monitorHeight / 2) - windowHeight / 2));
    	window.getContentPane().add(mainPanel);
    	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	window.setResizable(false);

    	JButton bgButton = new JButton("Change Background");
    	JButton exeButton = new JButton("Executable");
    	
    	mainPanel.add(bgButton);
    	mainPanel.add(exeButton);
    	
		bgButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				changeBackground();
			}
		});
		
		exeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				openExe();
			}
		});
    	
    	window.setVisible(true);
    }
    
    public static void changeBackground()
    {
    	JFileChooser imageSelector = new JFileChooser(System.getProperty("user.home") + "/Desktop/");
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("png/jpeg/gif", "jpg", "png", "JPEG", "gif");
    	imageSelector.setFileFilter(filter);
    	
        int returnValue = imageSelector.showOpenDialog(window);
        String imagePath;
        
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
        	System.out.println(getFileExtension(imageSelector.getSelectedFile()));
        	if (getFileExtension(imageSelector.getSelectedFile()).equals("jpg") || getFileExtension(imageSelector.getSelectedFile()).equals("png") || getFileExtension(imageSelector.getSelectedFile()).equals("gif"))
        	{
	            imagePath = imageSelector.getSelectedFile().getPath();
	            
	            User32.INSTANCE.SystemParametersInfo(0x0014, 0, imagePath, 1);
	            System.out.println(String.format("Changed desktop wallpaper to %s", imagePath));
        	} else {
        		new ErrorMessage("You can only select files with the extension jpg, png, or gif.");
        	}
        }
    }

    public static void openExe()
    {
        Frame mainFrame = new Frame("Pick Frame");
        mainFrame.setSize(0, 0);
        String ExePath = null;
        
        JFileChooser exeFile = new JFileChooser(System.getProperty("user.home") + "/Desktop/");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("exe", "exe", "Executable");
        exeFile.setFileFilter(filter);
        
        int returnValue = exeFile.showOpenDialog(mainFrame);
        
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
        	if (getFileExtension(exeFile.getSelectedFile()).equals("exe"))
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
        		new ErrorMessage("You must choose an executable file (.exe)");
        	}
        }
    }
    
    private static String getFileExtension(File file)
    {
        String extension = "";
 
        try
        {
            if (file != null && file.exists())
            {
                extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            }
        } catch (Exception e) {
            extension = "";
        }
 
        return extension;
    }
}
