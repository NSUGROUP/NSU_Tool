package com.Nullvalue.NSU_Tool;

import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

import mslinks.ShellLink;

public class Main
{
    private static JFrame window;
    private static boolean nearbyCheckState = false;
    
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
        int windowWidth = 240;
        int windowHeight = 200;
        
    	window = new JFrame("NSU Tool");
    	JPanel mainPanel = new JPanel();

    	window.setSize(windowWidth, windowHeight);
    	window.setLocation(((monitorWidth / 2) - windowWidth / 2), ((monitorHeight / 2) - windowHeight / 2));
    	window.getContentPane().add(mainPanel);
    	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	window.setResizable(false);
    	window.setIconImage(new ImageIcon(Main.class.getResource("/images/bbe.png")).getImage());

    	JButton bgButton = new JButton("Change Background");
    	JButton exeButton = new JButton("Add Executable");
    	JButton chromiumButton = new JButton("Install Chromium");
    	
    	JCheckBox nearbyFilesCheck = new JCheckBox("<html>Include files in parent<br>directory as dependencies<br></html>");
    	
    	mainPanel.add(bgButton);
    	mainPanel.add(exeButton);
    	mainPanel.add(nearbyFilesCheck);
    	mainPanel.add(chromiumButton);
    	
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
				addExe();
			}
		});
		
		nearbyFilesCheck.addItemListener(new ItemListener()
		{
		    public void itemStateChanged(ItemEvent e)
		    {
	        	nearbyCheckState = nearbyFilesCheck.isSelected();
		    }
		});
		
		chromiumButton.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
	        	installChromium();
		    }
		});
    	
    	window.setVisible(true);
    }
    
    public static void changeBackground()
    {
    	JFileChooser imageSelector = new JFileChooser(System.getProperty("user.home") + "/Desktop/");
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (.jpg, .png, .gif)", "jpg", "png", "JPEG", "gif");
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

    public static void addExe()
    {
    	if (new File(System.getenv("ProgramFiles(X86)") + "\\DRC INSIGHT Online Assessments").exists())
    	{
	        Frame mainFrame = new Frame("Pick Frame");
	        mainFrame.setSize(0, 0);
	        
	        JFileChooser exeFile = new JFileChooser(System.getProperty("user.home") + "/Desktop/");
	        FileNameExtensionFilter filter = new FileNameExtensionFilter("Executable (.exe)", "exe", "Executable");
	        exeFile.setFileFilter(filter);
	        
	        int returnValue = exeFile.showOpenDialog(mainFrame);
	        
	        if (returnValue == JFileChooser.APPROVE_OPTION)
	        {
	        	if (getFileExtension(exeFile.getSelectedFile()).equals("exe"))
	        	{
	        		if (nearbyCheckState == true)
		        	{
		        		String nameNoExtension = exeFile.getSelectedFile().getName().substring(0, exeFile.getSelectedFile().getName().lastIndexOf('.'));
		        		String name = exeFile.getSelectedFile().getName();
		        		File exeDirectory = new File(System.getenv("ProgramFiles(X86)") + "\\DRC INSIGHT Online Assessments\\" + nameNoExtension);
		        		
		        		if (!exeDirectory.exists())
		        		{
		        			System.out.println(exeDirectory.mkdir());
		        		}
		        		
		        		if(exeDirectory.isDirectory())
		        		{
		        		    File[] content = new File(exeFile.getSelectedFile().getParent()).listFiles();
		        		    for(int i = 0; i < content.length; i++)
		        		    {
		        		    	if (!content[i].isDirectory())
		        		    	{
		        		    		try
									{
										Files.copy(Paths.get(content[i].getPath()), Paths.get(exeDirectory.getPath() + "\\" + content[i].getName()));
									} catch (Exception e) {
										new ErrorMessage(e);
										break;
									}
		        		    	} else {
		        		    		try
									{
										FileUtils.moveDirectoryToDirectory(new File(content[i].getPath()), new File(exeDirectory.getPath()), true);
									} catch (IOException e)
									{
										new ErrorMessage(e);
									}
		        		    	}
		        		    }
		        		    
		        		    File newExe = new File(exeDirectory + "\\" + name);
		        		    try
							{
								ShellLink.createLink(newExe.getPath(), System.getProperty("user.home") + "/Desktop/" + nameNoExtension + ".lnk");
								new ErrorMessage("Short cut to \"" + name + "\" created and added to the desktop.");
							} catch (Exception e) {
								new ErrorMessage(e, "Error creating shortcut");
							}
		        		}
		        	} else {
		        		String nameNoExtension = exeFile.getSelectedFile().getName().substring(0, exeFile.getSelectedFile().getName().lastIndexOf('.'));
		        		String name = exeFile.getSelectedFile().getName();
		        		File exeDirectory = new File(System.getenv("ProgramFiles(X86)") + "\\DRC INSIGHT Online Assessments\\" + nameNoExtension);
		        		
		        		if (!exeDirectory.exists())
		        		{
		        			System.out.println(exeDirectory.mkdir());
		        		}
		        		
	        		    File content = new File(exeFile.getSelectedFile().getPath());
	        		    
        		        try
						{
							Files.copy(Paths.get(content.getPath()), Paths.get(exeDirectory.getPath() + "\\" + content.getName()));
						} catch (Exception e) {
							new ErrorMessage(e);
						}
	        		    
	        		    File newExe = new File(exeDirectory + "\\" + name);
	        		    try
						{
							ShellLink.createLink(newExe.getPath(), System.getProperty("user.home") + "/Desktop/" + nameNoExtension + ".lnk");
							new ErrorMessage("Short cut to \"" + name + "\" created and added to the desktop.");
						} catch (Exception e) {
							new ErrorMessage(e, "Error creating shortcut");
						}
		        	}
	        	} else {
	        		new ErrorMessage("You must choose an executable file (.exe)");
	        	}
	        }
    	} else {
    		new ErrorMessage("This tool must be run on an OCPS distributed machine to function.");
    	}
    }
    
    public static void installChromium()
    {
    	if (new File(System.getenv("ProgramFiles(X86)") + "\\DRC INSIGHT Online Assessments").exists() && !(new File(System.getenv("ProgramFiles(X86)") + "\\DRC INSIGHT Online Assessments\\Chromium-70.0.3538.9").exists()))
    	{
    		String drcFolder = System.getenv("ProgramFiles(X86)") + "\\DRC INSIGHT Online Assessments\\";
    		
    		try
			{
    			FileUtils.copyURLToFile(new URL("https://github.com/free-vpn/chrome/releases/download/70.0.3538.9/Chromium-Windows-70.0.3538.9.zip"), new File(drcFolder + "Chromium VPN.zip"), 30000, 5000000);
				
				int BUFFER = 2048;
		        File file = new File(drcFolder + "Chromium VPN.zip");

		        @SuppressWarnings("resource")
				ZipFile zip = new ZipFile(file);
		        String newPath = System.getenv("ProgramFiles(X86)") + "\\DRC INSIGHT Online Assessments\\";

		        new File(newPath).mkdir();
		        Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

		        while (zipFileEntries.hasMoreElements())
		        {
		            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
		            String currentEntry = entry.getName();

		            File destFile = new File(newPath, currentEntry);
		            File destinationParent = destFile.getParentFile();

		            destinationParent.mkdirs();

		            if (!entry.isDirectory())
		            {
		                BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
		                int currentByte;
		                byte data[] = new byte[BUFFER];

		                FileOutputStream fos = new FileOutputStream(destFile);
		                BufferedOutputStream dest = new BufferedOutputStream(fos,
		                BUFFER);

		                while ((currentByte = is.read(data, 0, BUFFER)) != -1)
		                {
		                    dest.write(data, 0, currentByte);
		                }
		                
		                dest.flush();
		                dest.close();
		                is.close();
		            }
		        }
		        
		        ShellLink.createLink(new File(drcFolder + "Chromium-70.0.3538.9\\chrome.exe").getPath(), System.getProperty("user.home") + "/Desktop/Chromium.lnk");
		        new ErrorMessage("Chromium successfully installed, shortcut added to the desktop.");
			} catch (Exception e) {
				new ErrorMessage(e);
			}
    	} else {
    		new ErrorMessage("This tool must be run on an OCPS distributed machine to function or Chromium is already installed.");
    	}
    }
    
    public static String getFileExtension(File file)
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