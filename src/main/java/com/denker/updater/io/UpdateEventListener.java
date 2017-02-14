//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

public interface UpdateEventListener 
{
    public void onDownloadPatch(AppVersion state);
    
    public void onUnpackPatch(AppVersion state);
    
    public void onPatchCleanUp(AppVersion state);
}
