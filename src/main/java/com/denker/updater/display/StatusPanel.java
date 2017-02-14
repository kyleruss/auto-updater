//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.display;

import com.denker.updater.io.AppVersion;
import com.denker.updater.io.UpdateEventListener;
import com.denker.updater.io.UpdateException;
import com.denker.updater.io.UpdateIterator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class StatusPanel extends JPanel
{
    private BufferedImage panelImage;
    private final JLabel statusIconLabel;
    private UpdateIterator updater;
    
    public StatusPanel()
    {
        setBackground(new Color(255, 255, 255, 0));
        setPreferredSize(new Dimension(449, 75));
        initImageContent();
        
        statusIconLabel =   new JLabel();
        statusIconLabel.setIcon(new ImageIcon("data/images/spinner.gif"));
        statusIconLabel.setForeground(Color.WHITE);
        statusIconLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        setStatus("Checking for updates");
        
        add(Box.createRigidArea(new Dimension(449, 5)));
        add(statusIconLabel);
        initUpdater();
    }
    
    private void initUpdater()
    {
        try
        {
            updater             =   new UpdateIterator();
            setStatus("Checking for updates");
            boolean hasUpdates  =   updater.checkForUpdates();
            updater.getUpdateHandler().setUpdateEventListener(new UpdateListener());
            
            if(hasUpdates)
            {
                int numUpdates  =   updater.getNumUpdates();
                setStatus(numUpdates + " updates found");
            }
            
            else setStatus("No updates found");
        }
        
        catch(UpdateException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] " + e.getMessage());
        }
    }
    
    public void setStatus(String status)
    {
        status  =   status.toUpperCase();
        statusIconLabel.setText(status);
    }
    
    public UpdateIterator getUpdater()
    {
        return updater;
    }
    
    private void initImageContent()
    {
        try
        {
            panelImage =   ImageIO.read(new File("data/images/bottom_panel.png"));
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to load image contents");
        }
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(panelImage, 0, 0, null);
    }
    
    private class UpdateListener implements UpdateEventListener
    {

        @Override
        public void onDownloadPatch(AppVersion state)
        {
            setStatus("Downloading patch");
        }

        @Override
        public void onUnpackPatch(AppVersion state) 
        {
            setStatus("Unpacking patch");
        }

        @Override
        public void onPatchCleanUp(AppVersion state) 
        {
            setStatus("Cleaning up patch contents");
        }
        
    }
}
