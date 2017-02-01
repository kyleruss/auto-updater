//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.display;

public class UpdaterWindow 
{
    private static UpdaterWindow instance;
    
    private UpdaterWindow() {}
    
    public static UpdaterWindow getInstance()
    {
        if(instance == null) instance = new UpdaterWindow();
        return instance;
    }
}
