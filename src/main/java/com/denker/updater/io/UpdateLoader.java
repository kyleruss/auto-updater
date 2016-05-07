//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

import org.apache.commons.net.ftp.FTPClient;

public class UpdateLoader
{

    public UpdateLoader()
    {
    }


    protected FTPClient getClient()
    {
        return client;
    }
}
