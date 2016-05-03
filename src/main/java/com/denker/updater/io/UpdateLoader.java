//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;

public class UpdateLoader
{
    private UpdateLoader() {}

    public boolean hasUpdateAvailable()
    {
        try
        {
            ClientConnector connector = ClientConnector.getInstance();
            connector.connect();
            FTPClient client = connector.getClient();

            InputStream is = client.retrieveFileStream("version.xml");
            AppVersion ver  =   AppVersion.getVersionFromFile(is);
            System.out.println(ver);
        }

        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }

        return false;
    }


    public static void main(String[] args)
    {
        UpdateLoader loader =   new UpdateLoader();
        loader.hasUpdateAvailable();
    }
}
