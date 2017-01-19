//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class FTPConfig
{
    public static final String CONFIG_DIR       =   "data/conf/";
    public static final String CONFIG_PATH      =   CONFIG_DIR + "config.xml";
    private static FTPConfig instance;
    private String host;
    private int port;
    private String user;
    private String password;
    private String workDirectory;
    private String versionPath;
    private String outputDirectory;
    private String patchDirectory;
    private String logName;
    private String logDir;
    private int maxLogSize;
    private int maxLogCount;
    private boolean keepPatches;

    private FTPConfig()
    {
        initConfig();
    }

    private Document getConfigDocument() throws ParserConfigurationException, IOException, SAXException
    {
        File file   =   new File(CONFIG_PATH);
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
    }

    private void initConfig()
    {
        try
        {
            Document configDoc      =   getConfigDocument();
            this.host               =   configDoc.getElementsByTagName("hostname").item(0).getTextContent();
            this.port               =   Integer.parseInt(configDoc.getElementsByTagName("port").item(0).getTextContent());
            this.user               =   configDoc.getElementsByTagName("username").item(0).getTextContent();
            this.password           =   configDoc.getElementsByTagName("password").item(0).getTextContent();
            this.workDirectory      =   configDoc.getElementsByTagName("working-dir").item(0).getTextContent();
            this.versionPath        =   configDoc.getElementsByTagName("version-path").item(0).getTextContent();
            this.outputDirectory    =   configDoc.getElementsByTagName("output-dir").item(0).getTextContent();
            this.patchDirectory     =   configDoc.getElementsByTagName("patch-dir").item(0).getTextContent();
            this.keepPatches        =   configDoc.getElementsByTagName("keep-patches").item(0).getTextContent().equalsIgnoreCase("true");
            this.logName            =   configDoc.getElementsByTagName("log-name").item(0).getTextContent();
            this.logDir             =   configDoc.getElementsByTagName("log-dir").item(0).getTextContent();
            this.maxLogSize         =   Integer.parseInt(configDoc.getElementsByTagName("max-log-size").item(0).getTextContent());
            this.maxLogCount        =   Integer.parseInt(configDoc.getElementsByTagName("max-log-count").item(0).getTextContent());
        }

        catch(Exception e)
        {
            System.out.println("[Error] Unable to read updater config");
        }
    }

    public static FTPConfig getInstance()
    {
        if(instance == null) instance = new FTPConfig();
        return instance;
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }

    public String getUser()
    {
        return user;
    }

    public String getPassword()
    {
        return password;
    }

    public String getWorkDirectory()
    {
        return workDirectory;
    }

    public boolean isRootDirectory()
    {
        return workDirectory == null || workDirectory.equals("/");
    }

    public String getVersionPath()
    {
        return versionPath;
    }

    public String getOutputDirectory()
    {
        return outputDirectory;
    }
    
    public String getPatchDirectory()
    {
        return patchDirectory;
    }
    
    public boolean isKeepPatches()
    {
        return keepPatches;
    }

    public String getLogName() 
    {
        return logName;
    }

    public String getLogDir() 
    {
        return logDir;
    }

    public int getMaxLogSize() 
    {
        return maxLogSize;
    }

    public int getMaxLogCount() 
    {
        return maxLogCount;
    }
    
    @Override
    public String toString()
    {
        return "[Connection]: " + host + ":" + port + "\n[Account] User: " + user + "; Password: " + password +
                "\n[Working Directory]: " + workDirectory + "\n [Version path]: " + versionPath;
    }
}
