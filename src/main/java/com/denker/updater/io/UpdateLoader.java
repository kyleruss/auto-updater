//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.lingala.zip4j.core.ZipFile;

public class UpdateLoader
{
    private AppVersion clientVersion, serverVersion;
    private FTPClient client;
    
    public UpdateLoader()
    {
        initClient();
        initVersions();
    }
    
    private void initClient()
    {
        try
        {
            ClientConnector connector = ClientConnector.getInstance();
            connector.connect();
            client    =   connector.getClient();
        }
        
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void initServerVersion()
    {
        try
        {
            InputStream is      =   client.retrieveFileStream("version.xml");
            serverVersion       =   AppVersion.getVersionFromFile(is);
        }

        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public boolean isConnected()
    {
        return client != null && client.isConnected();
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

    private void initVersions()
    {
        if(isConnected())
        {
            initServerVersion();
            initClientVersion();
        }
    }
    
    private boolean downloadPatchZip(String patchFile)
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

            return output.exists();
        }
        
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
   private boolean unpackPatch(String patchFile)
   {
       try
       {
           final String PATCH_DIR   =   FTPConfig.getInstance().getPatchDirectory();
           final String PATCH_PATH  =   PATCH_DIR + patchFile;
           final String OUT_DIR     =   FTPConfig.getInstance().getOutputDirectory();
           String absPatchPath      =   new File(PATCH_PATH).getAbsolutePath();
           
           ZipFile zFile            =   new ZipFile(absPatchPath);
           zFile.extractAll(OUT_DIR);
           
           return true;
       }
       
       catch(Exception e)
       {
           e.printStackTrace();
           return false;
       }
   }
   
   private boolean removePatchFile(String patchFile)
   {
        final String PATCH_DIR   =   FTPConfig.getInstance().getPatchDirectory();
        
        try
        {
            Path path                =   Paths.get(PATCH_DIR, patchFile);
            Files.delete(path);
            return true;
        }
        
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
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
//            downloadPatchZip("testzip.zip");
            unpackPatch("testzip.zip");
            //removePatchFile("testzip.zip");
        }
    }

    public static void main(String[] args)
    {
        UpdateLoader loader =   new UpdateLoader();
        loader.update();
    }
}

