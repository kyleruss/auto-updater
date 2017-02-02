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
    
    private UpdaterPanel() 
    {
        super(new BorderLayout());
        splashPanel =   new SplashPanel();
        setBackground(new Color(255, 255, 255, 0));
        
        add(splashPanel, BorderLayout.CENTER);
    }
    
    public SplashPanel getSplashPanel()
    {
        return splashPanel;
    }
    
    public static UpdaterPanel getInstance()
    {
        if(instance == null) instance = new UpdaterPanel();
        return instance;
    }
}
