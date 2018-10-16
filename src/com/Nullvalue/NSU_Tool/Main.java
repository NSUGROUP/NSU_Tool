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
        int windowHeight = 204;
        
    	window = new JFrame("NSU Tool");
    	JPanel mainPanel = new JPanel();

    	window.setSize(windowWidth, windowHeight);
    	window.setLocation(((monitorWidth / 2) - windowWidth / 2), ((monitorHeight / 2) - windowHeight / 2));
    	window.getContentPane().add(mainPanel);
    	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	window.setResizable(false);
    	window.setIconImage(new ImageIcon(Main.class.getResource("/images/icon.png")).getImage());

    	JButton bgButton = new JButton("Change Background");
    	JButton exeButton = new JButton("Add Executable");
    	JCheckBox nearbyFilesCheck = new JCheckBox("<html>Include files in parent<br>directory as dependencies<br></html>");
    	JButton chromiumButton = new JButton("Install Chromium");
    	JButton torButton = new JButton("Install Tor Browser");
    	
    	// https://github.com/NullvaIue/NSU_Tool/releases/download/v0.5.1/Tor.Browser.zip
    	
    	nearbyFilesCheck.setToolTipText("<html>This indicates whether the files in the same<br>folder as the executable are dependencies<br>that the executable can't run without.</html>");
    	chromiumButton.setToolTipText("<html>Chromium is a unrestricted, portable, open-source<br>version of Chrome. Meaning that you can install<br>extensions or change settings that are usually restricted.");
    	
    	mainPanel.add(bgButton);
    	mainPanel.add(exeButton);
    	mainPanel.add(nearbyFilesCheck);
    	mainPanel.add(chromiumButton);
    	mainPanel.add(torButton);
    	
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
		
		torButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				installTor();
			}
		});
    	
    	window.setVisible(true);
    }
    
    public static void changeBackground()
    {
    	JFileChooser imageSelector = new JFileChooser(System.getProperty("user.home") + "/Desktop/");
    	imageSelector.setFileFilter(new FileNameExtensionFilter("Images (.jpg, .png, .gif)", "jpg", "png", "JPEG", "gif"));
    	
        if (imageSelector.showOpenDialog(window) == JFileChooser.APPROVE_OPTION)
        {
        	File selectedFile = imageSelector.getSelectedFile();
        	String fileExtension = getFileExtension(selectedFile);
        	
        	if (fileExtension.equals("jpg") || fileExtension.equals("png") || fileExtension.equals("gif"))
	            User32.INSTANCE.SystemParametersInfo(0x0014, 0, selectedFile.getPath(), 1);
        	else
        		new ConfirmationMessage("You can only select files with the extension jpg, png, or gif.");
        }
    }

    public static void addExe()
    {
    	File drcFolderFile = new File(System.getenv("ProgramFiles(X86)") + "\\DRC INSIGHT Online Assessments");
    	
    	if (drcFolderFile.exists())
    	{
	        Frame mainFrame = new Frame();
	        mainFrame.setSize(0, 0);
	        
	        JFileChooser exeFile = new JFileChooser(System.getProperty("user.home") + "/Desktop/");
	        exeFile.setFileFilter(new FileNameExtensionFilter("Executable (.exe)", "exe", "Executable"));
	        
	        if (exeFile.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION)
	        {
	        	File selectedFile = exeFile.getSelectedFile();
	        	
	        	if (getFileExtension(selectedFile).equals("exe"))
	        	{
	        		String nameNoExtension = selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf('.'));
	        		String name = selectedFile.getName();
	        		File exeDirectory = new File(drcFolderFile + "\\" + nameNoExtension);
	        		
	        		if (nearbyCheckState == true)
		        	{
		        		if (!exeDirectory.exists())
		        			exeDirectory.mkdir();
		        		
		        		if(exeDirectory.isDirectory())
		        		{
		        		    File[] content = new File(selectedFile.getParent()).listFiles();
		        		    for(int i = 0; i < content.length; i++)
		        		    {
		        		    	try
		        		    	{
			        		    	if (content[i].isDirectory())
										FileUtils.moveDirectoryToDirectory(new File(content[i].getPath()), new File(exeDirectory.getPath()), true);
			        		    	else
			        		    		Files.copy(Paths.get(content[i].getPath()), Paths.get(exeDirectory.getPath() + "\\" + content[i].getName()));
			        		    	
		        		    	} catch (Exception e) {
		        		    		new ErrorMessage(e);
		        		    		break;
		        		    	}
		        		    }
		        		    
		        		    File newExe = new File(exeDirectory + "\\" + name);
		        		    
		        		    try
							{
								ShellLink.createLink(newExe.getPath(), System.getProperty("user.home") + "/Desktop/" + nameNoExtension + ".lnk");
								new ConfirmationMessage("Short cut to \"" + name + "\" created and added to the desktop.");
							} catch (Exception e) {
								new ErrorMessage(e, "Error creating shortcut");
							}
		        		}
		        	} else {
		        		if (!exeDirectory.exists())
		        			exeDirectory.mkdir();
		        		
	        		    File content = new File(selectedFile.getPath());
	        		    
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
							new ConfirmationMessage("Short cut to \"" + name + "\" created and added to the desktop.");
						} catch (Exception e) {
							new ErrorMessage(e, "Error creating shortcut");
						}
		        	}
	        	} else {
	        		new ConfirmationMessage("You must choose an executable file (.exe)");
	        	}
	        }
    	} else {
    		new ConfirmationMessage("This tool must be run on an OCPS distributed machine to function.");
    	}
    }

    public static void installChromium()
    {
		String dlURL = "https://download-chromium.appspot.com/dl/Win_x64?type=snapshots";
    	String folderName = "chrome-win";
		String dlName = "Chromium";
		String exeName = "chrome";
    	
		installExeFromURL(dlURL, folderName, dlName, exeName);
    }
    
    public static void installTor()
    {
		String dlURL = "https://github.com/NullvaIue/NSU_Tool/releases/download/v0.5.1/Tor.Browser.zip";
    	String folderName = "tor-browser";
		String dlName = "Tor Browser";
		String exeName = "firefo";
    	
		installExeFromURL(dlURL, folderName, dlName, exeName);
    }
    
    public static void installExeFromURL(String dlURL, String folderName, String dlName, String exeName)
    {
    	String drcFolder = System.getenv("ProgramFiles(X86)") + "\\DRC INSIGHT Online Assessments\\";
    	
    	if (new File(System.getenv("ProgramFiles(X86)") + "\\DRC INSIGHT Online Assessments").exists())
    	{
    		if (!(new File(System.getenv("ProgramFiles(X86)") + "\\DRC INSIGHT Online Assessments\\" + folderName).exists()))
    		{
	    		try
				{
	    			FileUtils.copyURLToFile(new URL(dlURL), new File(drcFolder + dlName + ".zip"), 30000, 300000);
					
					int BUFFER = 2048;
			        File file = new File(drcFolder + dlName + ".zip");
	
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
			                BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
	
			                while ((currentByte = is.read(data, 0, BUFFER)) != -1)
			                    dest.write(data, 0, currentByte);
			                
			                dest.flush();
			                dest.close();
			                is.close();
			            }
			        }
			        
			        ShellLink.createLink(new File(drcFolder + folderName + "\\" + exeName + ".exe").getPath(), System.getProperty("user.home") + "/Desktop/" + dlName + ".lnk");
			        new ConfirmationMessage(dlName + " successfully installed, a shortcut has been added to the desktop.");
				} catch (Exception e) {
					new ErrorMessage(e);
				}
	    	} else {
	    		new ConfirmationMessage(dlName + " is already installed.");
	    	}
    	} else {
    		new ConfirmationMessage("This tool must be run on an OCPS distributed machine to function.") ;
    	}
    }
    
    public static String getFileExtension(File file)
    {
        String extension = "";
 
        try
        {
            if (file != null && file.exists())
                extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        } catch (Exception e) {
            extension = "";
        }
 
        return extension;
    }
}