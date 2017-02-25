//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.display;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;

public class UpdaterPanel extends JPanel
{
    private static UpdaterPanel instance;
    private final SplashPanel splashPanel;
    private final StatusPanel statusPanel;
    
    private UpdaterPanel() 
    {
        super(new BorderLayout());
        setBackground(new Color(255, 255, 255, 0));
        
        splashPanel =   new SplashPanel();
        statusPanel =   new StatusPanel();
        
        add(splashPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    public SplashPanel getSplashPanel()
    {
        return splashPanel;
    }
    
    public StatusPanel getStatusPanel()
    {
        return statusPanel;
    }
    
    public static UpdaterPanel getInstance()
    {
        if(instance == null) instance = new UpdaterPanel();
        return instance;
    }
}
