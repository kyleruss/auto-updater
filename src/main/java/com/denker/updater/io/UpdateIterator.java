//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTPClient;

public class UpdateIterator 
{
    private AppVersion clientVersion, serverVersion;
    private FTPClient client;
    
    private void initClient() throws UpdateException
    {
        ClientConnector connector = ClientConnector.getInstance();
        connector.connect();
        client    =   connector.getClient();
    }

    private void initServerVersion() throws UpdateException
    {
        try
        {
            InputStream is      =   client.retrieveFileStream("version.xml");
            serverVersion       =   AppVersion.getVersionFromFile(is);
        }

        catch(IOException e)
        {
            throw new UpdateException(UpdateException.ErrorCode.SVERSION_CHECK_FAIL);
        }
    }
    
    public boolean isConnected()
    {
        return client != null && client.isConnected();
    }

    protected void initClientVersion() throws UpdateException
    {
        try
        {
            String path     =   FTPConfig.getInstance().getVersionPath();
            File file       =   new File(path);
            clientVersion   =   AppVersion.getVersionFromFile(new FileInputStream(file));
        }

        catch(IOException e)
        {
            throw new UpdateException(UpdateException.ErrorCode.CVERSION_CHECK_FAIL);
        }
    }

    private void initVersions() throws UpdateException
    {
        if(isConnected())
        {
            initServerVersion();
            initClientVersion();
        }
    }
    
    public boolean hasUpdateAvailable()
    {
        if(clientVersion == null || serverVersion == null)
            return false;

        else
            return clientVersion.getBuildID() < serverVersion.getBuildID();
    }
}
