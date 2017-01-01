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
    
    public UpdateHandler() throws UpdateException
    {
        initClient();
    }
    
    private void initClient() throws UpdateException
    {
        ClientConnector connector = ClientConnector.getInstance();
        connector.connect();
        client    =   connector.getClient();
    }
   
    
    private void downloadPatchZip() throws UpdateException
    {
        try
        {
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            final String PATCH_DIR          =   FTPConfig.getInstance().getPatchDirectory();
            File output                     =   new File(PATCH_DIR + "/" + statePath);
            try (FileOutputStream outputStream = new FileOutputStream(output.getAbsolutePath())) 
            {
                client.retrieveFile(statePath, outputStream);
            }

            if(!output.exists())
                throw new UpdateException(ErrorCode.PATCH_DL_ERR);
        }
        
        catch(IOException e)
        {
            throw new UpdateException(ErrorCode.PATCH_DL_ERR);
        }
    }
    
   private void unpackPatch() throws UpdateException
   {
       try
       {
           final String PATCH_DIR   =   FTPConfig.getInstance().getPatchDirectory();
           final String PATCH_PATH  =   PATCH_DIR + statePath;
           final String OUT_DIR     =   FTPConfig.getInstance().getOutputDirectory();
           String absPatchPath      =   new File(PATCH_PATH).getAbsolutePath();
           
           ZipFile zFile            =   new ZipFile(absPatchPath);
           zFile.extractAll(OUT_DIR);
       }
       
       catch(Exception e)
       {
           throw new UpdateException(ErrorCode.UNPACK_PATCH_ERR);
       }
   }
   
   private void removePatchFile() throws UpdateException
   {
        final String PATCH_DIR   =   FTPConfig.getInstance().getPatchDirectory();
        
        try
        {
            Path path                =   Paths.get(PATCH_DIR, statePath);
            Files.delete(path);
        }
        
        catch(IOException e)
        {
            throw new UpdateException(ErrorCode.REMOVE_PATCH_ERR);
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
    }
}

