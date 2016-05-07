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

    private ClientConnector() {}

    public FTPClient connect()
    {
        try
        {
            FTPClient client    =   new FTPClient();
            client.connect("localhost", 201);
            client.login("username", "password");
            return client;
        }

        catch(IOException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ClientConnector getInstance()
    {
        if(instance == null) instance   =   new ClientConnector();
        return instance;
    }

    public static void main(String[] args)
    {
        ClientConnector connector   =   getInstance();
        FTPClient client            =   connector.connect();

        if(client != null)
        {
            try
            {
                BufferedReader reader   =   new BufferedReader(new InputStreamReader(client.retrieveFileStream("/version.txt")));
                String line;

                while((line = reader.readLine()) != null)
                    System.out.println(line);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
