package com.denker.updater.io;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppVersion implements Comparable<AppVersion>
{
    private int buildID;
    private Date buildDate;

    public AppVersion(int buildID, Date buildDate)
    {
        this.buildID    =   buildID;
        this.buildDate  =   buildDate;
    }

    public int getBuildID()
    {
        return buildID;
    }

    public Date getBuildDate()
    {
        return buildDate;
    }

    public int compareTo(AppVersion other)
    {
        return new Integer(buildID).compareTo(other.getBuildID());
    }

    public String toString()
    {
        return "[Build]: " + buildID + "\n[Build Date]: " + buildDate;
    }

    public static AppVersion getVersionFromFile(InputStream is)
    {
        try
        {
            Document versDocument       =   DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            int fileBuildID             =   Integer.parseInt(versDocument.getElementsByTagName("build-id").item(0).getTextContent());
            String fileBuildDate        =   versDocument.getElementsByTagName("build-date").item(0).getTextContent();

            SimpleDateFormat dateFmt    =   new SimpleDateFormat("dd-MM-yyy");
            Date buildDateFmt           =   dateFmt.parse(fileBuildDate);

            return new AppVersion(fileBuildID, buildDateFmt);
        }

        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
