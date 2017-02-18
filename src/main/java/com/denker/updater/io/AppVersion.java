//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

import java.io.File;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

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
        int buildNameType   =   FTPConfig.getInstance().getBuildNameType();
        
        if(buildNameType == 0)
        {
            int current     =   Integer.parseInt(buildID);
            int otherID     =   Integer.parseInt(other.getBuildID());
            return Integer.compare(current, otherID);
        }
        
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
            return null;
        }
    }
    
    public void saveVersionToClient() throws UpdateException
    {
        try
        {
            DocumentBuilderFactory docFac   =   DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder      =   docFac.newDocumentBuilder();
            
            Document doc                    =   docBuilder.newDocument();
            Element rootElement             =   doc.createElement("app-version");
            doc.appendChild(rootElement);
            
            Element buildIDElement          =   doc.createElement("build-id");
            buildIDElement.appendChild(doc.createTextNode(buildID));
            rootElement.appendChild(buildIDElement);
            
            Element buildDateElement        =   doc.createElement("build-date");
            buildDateElement.appendChild(doc.createTextNode(buildDate.toString()));
            rootElement.appendChild(buildDateElement);
            
            TransformerFactory tFactory     =   TransformerFactory.newInstance();
            Transformer transformer         =   tFactory.newTransformer();
            DOMSource src                   =   new DOMSource(doc);
            String versionPath              =   FTPConfig.getInstance().getVersionPath();
            StreamResult res                =   new StreamResult(new File(versionPath));
            
            transformer.transform(src, res);
        }
        
        catch(ParserConfigurationException | DOMException | TransformerException e)
        {
            throw new UpdateException(UpdateException.ErrorCode.REMOVE_PATCH_ERR, e);
        }
    }
}
