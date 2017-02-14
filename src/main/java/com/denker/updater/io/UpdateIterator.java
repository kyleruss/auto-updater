//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class UpdateIterator 
{
    private AppVersion clientVersion;
    private FTPClient client;
    private final UpdateHandler updateHandler;
    private List<AppVersion> buildList;
    private int position;
    
    public UpdateIterator() throws UpdateException
    {
        initClient();
        
        updateHandler   =   new UpdateHandler(client);   
        position        =   0;
    }
    
    private void initClient() throws UpdateException
    {
        ClientConnector connector = ClientConnector.getInstance();
        connector.connect();
        client    =   connector.getClient();
        
    }
    
    public boolean checkForUpdates() throws UpdateException
    {
        initClientVersion();
        initBuildList();
        return hasUpdates();
    }
    
    public UpdateHandler getUpdateHandler()
    {
        return updateHandler;
    }
    
    private void initClientVersion() throws UpdateException
    {
        if(isConnected())
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
    }
    
    public void initBuildList() throws UpdateException
    {
        if(client != null)
        {
            try
            {
                String patchDir         =   FTPConfig.getInstance().getServerPatchDirectory();
                FTPFile[] buildFiles    =   client.listFiles(patchDir);
                buildList               =   new ArrayList<>();
                
                for(FTPFile buildFile : buildFiles)
                {
                    String fileName             =   buildFile.getName();
                    fileName                    =   fileName.substring(0, fileName.indexOf("."));
                    AppVersion nextVersion      =   new AppVersion(fileName);  
                    
                    if(clientVersion.compareTo(nextVersion) < 0)
                    {
                        buildList.add(nextVersion);
                        System.out.println(buildFile.getName());
                    }
                }
            }
            
            catch(IOException e)
            {
                throw new UpdateException(UpdateException.ErrorCode.SVERSION_CHECK_FAIL ,e);
            }
        }
    }
    
    public void nextUpdate() throws UpdateException
    {
        if(hasUpdates())
        {
            AppVersion nextUpdate   =   buildList.get(position);
            updateHandler.processPatch(nextUpdate);
            position++;
        }
    }
    
    public int getNumUpdates()
    {
        return buildList.size();
    }
    
    public boolean isConnected()
    {
        return client != null && client.isConnected();
    }
    
    public boolean hasUpdates()
    {
        return position < getNumUpdates();
    }
    
    public int getPosition()
    {
        return position;
    }
    
    public void reset()
    {
        position    =   0;
    }
    
    public static void main(String[] args)
    {
        try
        {
            UpdateIterator iterator =   new UpdateIterator();
            iterator.initBuildList();
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
