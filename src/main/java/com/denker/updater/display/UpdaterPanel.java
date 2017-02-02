//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.display;

import javax.swing.JPanel;

public class UpdaterPanel extends JPanel
{
    private static UpdaterPanel instance;
    private final SplashPanel splashPanel;
    
    private UpdaterPanel() 
    {
        splashPanel =   new SplashPanel();
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
