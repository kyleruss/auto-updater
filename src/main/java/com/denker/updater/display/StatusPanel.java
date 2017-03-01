//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.display;

import com.denker.updater.io.AppVersion;
import com.denker.updater.io.FTPConfig;
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
    public final int SERVICING_STATUS       =   0;
    public final int INFO_STATUS            =   1;
    public final int SUCCESS_STATUS         =   2;
    public final int ERROR_STATUS           =   3;
    public final int NO_UPDATES_STATUS      =   100;
    public final int SUCCESS_UPDATE_STATUS  =   200;
    public final int FAIL_UPDATE_STATUS     =   300;
    
    private BufferedImage panelImage;
    private final JLabel statusIconLabel;
    private UpdateIterator updater;
    private int currentStatusType;
    
    public StatusPanel()
    {
        setBackground(new Color(255, 255, 255, 0));
        setPreferredSize(new Dimension(449, 75));
        initImageContent();
        
        currentStatusType   =   -1;   
        statusIconLabel     =   new JLabel();
        statusIconLabel.setIcon(new ImageIcon("spinner.gif"));
        statusIconLabel.setForeground(Color.WHITE);
        statusIconLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        setStatus("Checking for updates", SERVICING_STATUS);
        
        add(Box.createRigidArea(new Dimension(449, 5)));
        add(statusIconLabel);
        setStatus("Preparing service", SERVICING_STATUS);
    }
    
    protected void startUpdater()
    {
        try
        {
            updater             =   new UpdateIterator();
            setStatus("Checking for updates", SERVICING_STATUS);
            boolean hasUpdates  =   updater.checkForUpdates();
            updater.getUpdateHandler().setUpdateEventListener(new UpdateListener());
            
            if(hasUpdates)
            {
                int numUpdates  =   updater.getNumUpdates();
                setStatus(numUpdates + " updates found", INFO_STATUS);
                getNextUpdate();
            }
            
            else 
            {
                setStatus("No updates found", INFO_STATUS);
                launchClosingApplication(NO_UPDATES_STATUS);
            }
        }
        
        catch(UpdateException e)
        {
            setStatus(e.getMessage(), ERROR_STATUS);
        }
    }
    
    public void getNextUpdate()
    {
        try
        {
            if(updater != null)
                updater.nextUpdate();
        }
        
        catch(UpdateException e)
        {
            setStatus(e.getMessage(), ERROR_STATUS);
        }
    }
    
    public void setStatus(String status, int statusType)
    {
        status  =   status.toUpperCase();
        statusIconLabel.setText(status);
        
        if(currentStatusType != statusType)
        {
            final String IMG_FOLDER =   "data/images/";
            
            switch(statusType)
            {
                case SERVICING_STATUS:
                    statusIconLabel.setIcon(new ImageIcon(IMG_FOLDER + "spinner.gif"));
                    break;
                    
                case INFO_STATUS:
                    statusIconLabel.setIcon(new ImageIcon(IMG_FOLDER + "notifications_small_icon.png"));
                    break;
                    
                case SUCCESS_STATUS:
                    statusIconLabel.setIcon(new ImageIcon(IMG_FOLDER + "successicon.png"));
                    break;
                    
                case ERROR_STATUS:
                    statusIconLabel.setIcon(new ImageIcon(IMG_FOLDER + "failicon.png"));
                    break;
            }
            
            currentStatusType = statusType;
        }
    }
    
    public UpdateIterator getUpdater()
    {
        return updater;
    }
    
    public String getProgressString()
    {
        int iteration   =   updater.getPosition() + 1;
        int total       =   updater.getNumUpdates();
        
        return "[" + iteration + "/" + total + "] ";
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
    
    public void launchClosingApplication(int statusType)
    {
        FTPConfig conf      =   FTPConfig.getInstance();
        boolean exitLaunch  =   conf.isEnableExitLaunch();
        
        if(exitLaunch)
        {
            String launchMessage    =   "Launching application";
            setStatus(launchMessage, SERVICING_STATUS);

            try
            {
                String appPath      =   conf.getExitLaunchPath();
                String argName      =   conf.getStatusArgName();
                String argValue     =   "" + statusType;
                
                new ProcessBuilder(appPath, argName, argValue).start();
            }

            catch(Exception e)
            {
                setStatus("Failed to launch application", ERROR_STATUS);
            }
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
            String message  =   "Downloading patch";
            setStatus(getProgressString() + message, SERVICING_STATUS);
        }

        @Override
        public void onUnpackPatch(AppVersion state) 
        {
            String message  =    "Unpacking patch";
            setStatus(getProgressString() + message, SERVICING_STATUS);
        }

        @Override
        public void onPatchCleanUp(AppVersion state) 
        {
            String message  =   "Cleaning up patch contents";
            setStatus(getProgressString() + message, SERVICING_STATUS);
        }

        @Override
        public void onUpdateComplete(AppVersion state) 
        {
            if(updater.hasUpdates())
                getNextUpdate();
            else
            {
                String message      =   "Updating complete";
                setStatus(message, SUCCESS_STATUS);
                
                launchClosingApplication(SUCCESS_UPDATE_STATUS);
            }
        }

        @Override
        public void onVersionUpdate(AppVersion state) 
        {
            String message  =   "Updating client version";
            setStatus(getProgressString() + message, SERVICING_STATUS);
        }
    }
}
