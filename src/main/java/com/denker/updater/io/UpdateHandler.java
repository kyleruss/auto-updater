//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

import com.denker.updater.io.UpdateException.ErrorCode;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.lingala.zip4j.core.ZipFile;

public class UpdateHandler
{
    private AppVersion stateVersion;
    private String statePath;
    private FTPClient client;
    private UpdateEventListener updateEventListener;
    
    public UpdateHandler(FTPClient client)
    {
        this.client  =   client;
    }
    
    private void downloadPatchZip() throws UpdateException
    {
        try
        {
            updateEventListener.onDownloadPatch(stateVersion);
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            final String PATCH_DIR          =   FTPConfig.getInstance().getClientPatchDirectory();
            final String SERVER_DIR         =   FTPConfig.getInstance().getServerPatchDirectory();
            File output                     =   new File(PATCH_DIR + "/" + statePath);
            
            try (FileOutputStream outputStream = new FileOutputStream(output.getAbsolutePath())) 
            {
                client.retrieveFile(SERVER_DIR + statePath, outputStream);
            }

            if(!output.exists())
                throw new UpdateException(ErrorCode.PATCH_DL_ERR, null);
        }
        
        catch(IOException e)
        {
            throw new UpdateException(ErrorCode.PATCH_DL_ERR, e);
        }
    }
    
   private void unpackPatch() throws UpdateException
   {
       try
       {
           updateEventListener.onUnpackPatch(stateVersion);
           final String PATCH_DIR   =   FTPConfig.getInstance().getClientPatchDirectory();
           final String PATCH_PATH  =   PATCH_DIR + statePath;
           final String OUT_DIR     =   FTPConfig.getInstance().getOutputDirectory();
           String absPatchPath      =   new File(PATCH_PATH).getAbsolutePath();
           
           ZipFile zFile            =   new ZipFile(absPatchPath);
           zFile.extractAll(OUT_DIR);
       }
       
       catch(Exception e)
       {
           e.printStackTrace();
           throw new UpdateException(ErrorCode.UNPACK_PATCH_ERR, e);
       }
   }
   
   private void removePatchFile() throws UpdateException
   {
        updateEventListener.onPatchCleanUp(stateVersion);
        final String PATCH_DIR   =   FTPConfig.getInstance().getClientPatchDirectory();

        try
        {
            Path path                =   Paths.get(PATCH_DIR, statePath);
            Files.delete(path);
        }

        catch(IOException e)
        {
            throw new UpdateException(ErrorCode.REMOVE_PATCH_ERR, e);
        }
   }

    
    private void initState(AppVersion stateVersion)
    {
        this.stateVersion   =   stateVersion;
        statePath           =   stateVersion.getBuildID() + ".zip";
    }

    public void processPatch(AppVersion stateVersion) throws UpdateException
    {
        initState(stateVersion);
        downloadPatchZip();
        unpackPatch();
        removePatchFile();
        updateEventListener.onUpdateComplete(stateVersion);
    }
    
    public AppVersion getStateVersion()
    {
        return stateVersion;
    }
    
    public void setUpdateEventListener(UpdateEventListener eventListener)
    {
        this.updateEventListener    =   eventListener;
    }
    
    public UpdateEventListener getUpdateEventListener()
    {
        return updateEventListener;
    }
    
    public FTPClient getClient()
    {
        return client;
    }
    
    public void setClient(FTPClient client)
    {
        this.client =   client;
    }
}

