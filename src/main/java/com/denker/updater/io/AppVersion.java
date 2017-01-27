//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppVersion implements Comparable<AppVersion>
{
    private final String buildID;
    private final Date buildDate;
    
    public AppVersion(String buildID)
    {
        this(buildID, new Date());
    }
    
    public AppVersion(String buildID, Date buildDate)
    {
        this.buildID    =   buildID;
        this.buildDate  =   buildDate;
    }

    public String getBuildID()
    {
        return buildID;
    }

    public Date getBuildDate()
    {
        return buildDate;
    }

    @Override
    public int compareTo(AppVersion other)
    {
        return buildID.compareTo(other.getBuildID());
    }

    @Override
    public String toString()
    {
        return "[Build]: " + buildID + "\n[Build Date]: " + buildDate;
    }

    public static AppVersion getVersionFromFile(InputStream is)
    {
        try
        {
            Document versDocument       =   DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            String fileBuildID          =   versDocument.getElementsByTagName("build-id").item(0).getTextContent();
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
