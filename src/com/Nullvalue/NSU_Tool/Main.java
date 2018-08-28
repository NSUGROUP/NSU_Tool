package com.Nullvalue.NSU_Tool;

import java.io.*;
import java.util.zip.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    	
    	int montiorWidth = gd.getDisplayMode().getWidth();
        int monitorHeight = gd.getDisplayMode().getHeight();
        int windowWidth = 350;
        int windowHeight = 200;
        
        ImageIcon icon = new ImageIcon("Resources/b.png");
        
    	window = new JFrame("NSU Tool");
    	JPanel mainPanel = new JPanel();
    	JButton bgButton = new JButton("Change Background");
    	JButton zipButton = new JButton("Unzip File");

    	window.setSize(windowWidth, windowHeight);
    	window.setIconImage(icon.getImage());
    	window.setLocation(((montiorWidth / 2) - windowWidth / 2), ((monitorHeight / 2) - windowHeight / 2));
    	window.getContentPane().add(mainPanel);
    	
    	mainPanel.add(bgButton);
    	mainPanel.add(zipButton);
    	
		bgButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				changeBackground();
			}
		});
		
		zipButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					unzipFile();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
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
    
    public static void unzipFile() throws Exception
    {
    	JFileChooser zipSelector = new JFileChooser();
        int returnValue = zipSelector.showOpenDialog(window);
        String folderPath = null;
        byte[] buffer = new byte[1024];
        
        int pos = zipSelector.getSelectedFile().getName().lastIndexOf(".");
        String name =  pos > 0 ? zipSelector.getSelectedFile().getName().substring(0, pos) : zipSelector.getSelectedFile().getName();
        String destDir = zipSelector.getSelectedFile().getParent();
        System.out.println(destDir);
        
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            folderPath = zipSelector.getSelectedFile().getPath();
            File dir = new File(destDir);
            
            if(!dir.exists()) dir.mkdirs();
            FileInputStream fis;
            
            try
            {
                fis = new FileInputStream(folderPath);
                ZipInputStream zis = new ZipInputStream(fis);
                ZipEntry ze = zis.getNextEntry();
                while(ze != null)
                {
                    String fileName = ze.getName();
                    File newFile = new File(destDir + File.separator + fileName);
                    new File(newFile.getParent()).mkdirs();
                    System.out.println("Unzipping to " + newFile.getAbsolutePath());

                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    
                    while ((len = zis.read(buffer)) > 0)
                    {
                    	fos.write(buffer, 0, len);
                    }
                    fos.close();

                    zis.closeEntry();
                    ze = zis.getNextEntry();
                }

                zis.closeEntry();
                zis.close();
                fis.close();
                
            } catch (IOException e)
            {
                e.printStackTrace();
            }
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
                    
                }
                catch (IOException e)
                {
                    System.err.println("Error occured while opening file...");
                    e.printStackTrace();
                }
            }else{
                System.out.println(String.format("Failed to open file %s", ExePath));
            }
        }
    }
}