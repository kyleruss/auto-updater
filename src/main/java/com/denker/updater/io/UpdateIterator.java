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
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class UpdateIterator 
{
    private AppVersion clientVersion, serverVersion;
    private FTPClient client;
    private UpdateHandler updateHandler;
    private List<AppVersion> buildList;
    
    public UpdateIterator() throws UpdateException
    {
        initClient();
    }
    
    private void initClient() throws UpdateException
    {
        ClientConnector connector = ClientConnector.getInstance();
        connector.connect();
        client    =   connector.getClient();
        
    }
    
    public UpdateHandler getUpdateHandler()
    {
        return updateHandler;
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
            throw new UpdateException(UpdateException.ErrorCode.SVERSION_CHECK_FAIL, e);
        }
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
            throw new UpdateException(UpdateException.ErrorCode.CVERSION_CHECK_FAIL, e);
        }
    }
    
    public void getBuildList() throws UpdateException
    {
        if(client != null)
        {
            try
            {
                FTPFile[] buildFiles    =   client.listFiles();
                buildList               =   new ArrayList<>();
                
                for(FTPFile buildFile : buildFiles)
                {
                    buildList.add(new AppVersion(buildFile.getName()));
                    System.out.println(buildFile.getName());
                }
            }
            
            catch(IOException e)
            {
                throw new UpdateException(UpdateException.ErrorCode.SVERSION_CHECK_FAIL ,e);
            }
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
    
    public boolean isConnected()
    {
        return client != null && client.isConnected();
    }
    
    public boolean hasUpdateAvailable()
    {
        if(clientVersion == null || serverVersion == null)
            return false;

        else
            return clientVersion.getBuildID().compareTo(serverVersion.getBuildID()) < 0;
    }
    
    public static void main(String[] args)
    {
        try
        {
            UpdateIterator iterator =   new UpdateIterator();
            iterator.getBuildList();
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
