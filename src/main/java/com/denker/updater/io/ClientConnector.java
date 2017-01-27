//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

import com.denker.updater.io.UpdateException.ErrorCode;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

public class ClientConnector
{
    private static ClientConnector instance;
    private FTPClient client;

    private ClientConnector() {}

    public void connect() throws UpdateException
    {
        FTPConfig config    =   FTPConfig.getInstance();
        client              =   new FTPClient();
        
        try
        {
            client.connect(config.getHost(), config.getPort());
            client.login(config.getUser(), config.getPassword());
        }
        
        catch(IOException e)
        {
            throw new UpdateException(ErrorCode.CLIENT_CONN_FAIL, e);
        }
    }

    public void disconnect() throws UpdateException
    {
        try
        {
            if(client != null)
                client.disconnect();

            else throw new UpdateException(ErrorCode.CLIENT_DISC_FAIL, null);
        }

        catch (IOException e)
        {
            throw new UpdateException(ErrorCode.CLIENT_DISC_FAIL, e);
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
