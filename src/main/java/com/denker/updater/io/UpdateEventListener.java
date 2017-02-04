//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

public interface UpdateEventListener 
{
    public void onVersionCheck();
    
    public void onDownloadPatch();
    
    public void onUnpackPatch();
    
    public void onPatchCleanUp();
}
