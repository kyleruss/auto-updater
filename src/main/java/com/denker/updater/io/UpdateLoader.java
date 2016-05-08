//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

import com.sun.deploy.util.SessionState;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

public class UpdateLoader
{
    private AppVersion clientVersion, serverVersion;

    public UpdateLoader()
    {
        initVersions();
    }

    private void initServerVersion()
    {
        try
        {
            ClientConnector connector = ClientConnector.getInstance();
            connector.connect();

            FTPClient client    =   connector.getClient();
            InputStream is      =   client.retrieveFileStream("version.xml");
            serverVersion       =   AppVersion.getVersionFromFile(is);
        }

        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    protected void initClientVersion()
    {
        try
        {
            String path     =   FTPConfig.getInstance().getVersionPath();
            File file       =   new File(path);
            clientVersion   =   AppVersion.getVersionFromFile(new FileInputStream(file));
        }

        catch(FileNotFoundException e)
        {
            System.out.println("[Error] Version file not found");
        }
    }

    protected void initVersions()
    {
        initServerVersion();
        initClientVersion();
    }

    public boolean hasUpdateAvailable()
    {
        if(clientVersion == null || serverVersion == null)
            return false;

        else
            return clientVersion.getBuildID() < serverVersion.getBuildID();
    }

    public void update()
    {
        if(hasUpdateAvailable())
        {

        }
    }

    public static void main(String[] args)
    {
        UpdateLoader loader =   new UpdateLoader();
        loader.hasUpdateAvailable();
    }
}
