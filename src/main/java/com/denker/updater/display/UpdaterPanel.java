//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.display;

public class UpdaterPanel 
{
    private static UpdaterPanel instance;
    
    private UpdaterPanel() {}
    
    public static UpdaterPanel getInstance()
    {
        if(instance == null) instance = new UpdaterPanel();
        return instance;
    }
}
