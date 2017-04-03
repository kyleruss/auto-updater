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

public class Config
{
    public static final String DATA_DIR         =   "data";
    public static final String CONFIG_DIR       =   "data/conf/";
    public static final String CONFIG_PATH      =   CONFIG_DIR + "config.xml";
    private static Config instance;
    private String host;
    private int port;
    private String user;
    private String password;
    private String versionPath;
    private String outputDirectory;
    private String clientPatchDirectory;
    private String serverPatchDirectory;
    private boolean enableLog;
    private String logName;
    private String logDir;
    private int maxLogSize;
    private int maxLogCount;
    private boolean keepPatches;
    private int buildNameType;
    private boolean enableExitLaunch;
    private String exitLaunchPath;
    private String statusArgName;
    private boolean forceLaunch;

    private Config()
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
            Document doc                =   getConfigDocument();
            this.host                   =   getStringConfig(doc, "hostname");
            this.port                   =   getIntegerConfig(doc, "port");
            this.user                   =   getStringConfig(doc, "username");
            this.password               =   getStringConfig(doc, "password");
            this.versionPath            =   getStringConfig(doc, "version-path");
            this.outputDirectory        =   getStringConfig(doc, "output-dir");
            this.clientPatchDirectory   =   getStringConfig(doc, "client-patch-dir");
            this.serverPatchDirectory   =   getStringConfig(doc, "server-patch-dir");
            this.keepPatches            =   getBooleanConfig(doc, "keep-patches");
            this.logName                =   getStringConfig(doc, "log-name");
            this.logDir                 =   getStringConfig(doc, "log-dir");
            this.maxLogSize             =   getIntegerConfig(doc, "max-log-size");
            this.maxLogCount            =   getIntegerConfig(doc, "max-log-count");
            this.enableLog              =   getBooleanConfig(doc, "enable-log");
            this.buildNameType          =   getIntegerConfig(doc, "build-name-type");
            this.enableExitLaunch       =   getBooleanConfig(doc, "enable-exit-launch");
            this.exitLaunchPath         =   getStringConfig(doc, "exit-launch-path");
            this.statusArgName          =   getStringConfig(doc, "status-arg-name");
            this.forceLaunch            =   getBooleanConfig(doc, "force-launch");
        }

        catch(Exception e)
        {
            System.out.println("[Error] Unable to read updater config");
        }
    }
    
    public static boolean getBooleanConfig(Document doc, String name)
    {
        final String BOOL_NAME   =   "true";
        return doc.getElementsByTagName(name).item(0).getTextContent().equalsIgnoreCase(BOOL_NAME);
    }
    
    public static String getStringConfig(Document doc, String name)
    {
        return doc.getElementsByTagName(name).item(0).getTextContent();
    }
    
    public static int getIntegerConfig(Document doc, String name)
    {
        return Integer.parseInt(doc.getElementsByTagName(name).item(0).getTextContent());
    }

    public static Config getInstance()
    {
        if(instance == null) instance = new Config();
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

    public String getVersionPath()
    {
        return versionPath;
    }

    public String getOutputDirectory()
    {
        return outputDirectory;
    }
    
    public String getClientPatchDirectory()
    {
        return clientPatchDirectory;
    }
    
    public String getServerPatchDirectory()
    {
        return serverPatchDirectory;
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

    public boolean isEnableLog() 
    {
        return enableLog;
    }
    
    public int getBuildNameType()
    {
        return buildNameType;
    }

    public boolean isEnableExitLaunch() 
    {
        return enableExitLaunch;
    }

    public String getExitLaunchPath()
    {
        return exitLaunchPath;
    }
    
    public String getStatusArgName()
    {
        return statusArgName;
    }
    
    public boolean isForceLaunch()
    {
        return forceLaunch;
    }
}
