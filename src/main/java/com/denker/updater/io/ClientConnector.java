//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

public class ClientConnector
{
    private static ClientConnector instance;
    private FTPClient client;

    private ClientConnector() {}

    public void connect() throws IOException
    {
        FTPConfig config    =   FTPConfig.getInstance();
        client              =   new FTPClient();
        client.connect(config.getHost(), config.getPort());
        client.login(config.getUser(), config.getPassword());
    }

    public boolean disconnect()
    {
        try
        {
            if(client != null)
            {
                client.disconnect();
                return true;
            }

            else return false;
        }

        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public FTPClient getClient()
    {
        return client;
    }

    public static ClientConnector getInstance()
    {
        if(instance == null) instance   =   new ClientConnector();
        return instance;
    }
}
