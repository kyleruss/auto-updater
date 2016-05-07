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
    private static FTPConfig instance;
    private String host;
    private int port;
    private String user;
    private String password;
    private String workDirectory;

    private FTPConfig()
    {
        initConfig();
    }

    private Document getConfigDocument() throws ParserConfigurationException, IOException, SAXException
    {
        File file   =   new File("data/config.xml");
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
    }

    private void initConfig()
    {

        try
        {
            Document configDoc  =   getConfigDocument();
            this.host           =   configDoc.getElementsByTagName("hostname").item(0).getTextContent();
            this.port           =   Integer.parseInt(configDoc.getElementsByTagName("port").item(0).getTextContent());
            this.user           =   configDoc.getElementsByTagName("username").item(0).getTextContent();
            this.password       =   configDoc.getElementsByTagName("password").item(0).getTextContent();
            this.workDirectory  =   configDoc.getElementsByTagName("workingDir").item(0).getTextContent();

            System.out.println(toString());
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

    @Override
    public String toString()
    {
        return "[Connection]: " + host + ":" + port + "\n[Account] User: " + user + "; Password: " + password + "\n[Working Directory]: " + workDirectory;
    }
}
