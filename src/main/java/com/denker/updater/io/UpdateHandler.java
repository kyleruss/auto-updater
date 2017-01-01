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
    private AppVersion clientVersion, serverVersion;
    private FTPClient client;
    
    public UpdateHandler() throws UpdateException
    {
        initClient();
        initVersions();
    }
    
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
            throw new UpdateException(ErrorCode.SVERSION_CHECK_FAIL);
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
            throw new UpdateException(ErrorCode.CVERSION_CHECK_FAIL);
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
    
    private void downloadPatchZip(String patchFile) throws UpdateException
    {
        try
        {
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            final String PATCH_DIR          =   FTPConfig.getInstance().getPatchDirectory();
            File output                     =   new File(PATCH_DIR + "/" + patchFile);
            try (FileOutputStream outputStream = new FileOutputStream(output.getAbsolutePath())) 
            {
                client.retrieveFile(patchFile, outputStream);
            }

            if(!output.exists())
                throw new UpdateException(ErrorCode.PATCH_DL_ERR);
        }
        
        catch(IOException e)
        {
            throw new UpdateException(ErrorCode.PATCH_DL_ERR);
        }
    }
    
   private void unpackPatch(String patchFile) throws UpdateException
   {
       try
       {
           final String PATCH_DIR   =   FTPConfig.getInstance().getPatchDirectory();
           final String PATCH_PATH  =   PATCH_DIR + patchFile;
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
   
   private void removePatchFile(String patchFile) throws UpdateException
   {
        final String PATCH_DIR   =   FTPConfig.getInstance().getPatchDirectory();
        
        try
        {
            Path path                =   Paths.get(PATCH_DIR, patchFile);
            Files.delete(path);
        }
        
        catch(IOException e)
        {
            throw new UpdateException(ErrorCode.REMOVE_PATCH_ERR);
        }
   }

    public boolean hasUpdateAvailable()
    {
        if(clientVersion == null || serverVersion == null)
            return false;

        else
            return clientVersion.getBuildID() < serverVersion.getBuildID();
    }

    public void processPatch() throws UpdateException
    {
        if(hasUpdateAvailable())
        {
            downloadPatchZip("testzip.zip");
            unpackPatch("testzip.zip");
            removePatchFile("testzip.zip");
        }
    }

    public static void main(String[] args)
    {
        try
        {
            UpdateHandler loader =   new UpdateHandler();
            loader.processPatch();
        }
        
        catch(UpdateException e)
        {
            System.out.println(e.getMessage());
        }
    }
}

