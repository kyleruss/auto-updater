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
import javax.xml.transform.OutputKeys;
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
    private final String buildVersion;
    
    public AppVersion(String buildID)
    {
        this(buildID, new Date(), "1.0");
    }
    
    public AppVersion(String buildID, String buildVersion)
    {
        this(buildID, new Date(), buildVersion);
    }
    
    public AppVersion(String buildID, Date buildDate, String buildVersion)
    {
        this.buildID        =   buildID;
        this.buildDate      =   buildDate;
        this.buildVersion   =   buildVersion;
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
        int buildNameType   =   Config.getInstance().getBuildNameType();
        
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
            String fileBuildID          =   Config.getStringConfig(versDocument, "build-id");
            String fileBuildDate        =   Config.getStringConfig(versDocument, "build-date");
            String fileVersion          =   Config.getStringConfig(versDocument, "build-version");

            SimpleDateFormat dateFmt    =   new SimpleDateFormat("dd-MM-yyy");
            Date buildDateFmt           =   dateFmt.parse(fileBuildDate);

            return new AppVersion(fileBuildID, buildDateFmt, fileVersion);
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
            
            Element buildVersElement        =   doc.createElement("build-version");
            buildVersElement.appendChild(doc.createTextNode(buildVersion));
            rootElement.appendChild(buildVersElement);
            
            
            SimpleDateFormat dateFmt        =   new SimpleDateFormat("dd-MM-yyy");
            String formattedDate            =   dateFmt.format(buildDate);
            Element buildDateElement        =   doc.createElement("build-date");
            buildDateElement.appendChild(doc.createTextNode(formattedDate));
            rootElement.appendChild(buildDateElement);
            
            TransformerFactory tFactory     =   TransformerFactory.newInstance();
            Transformer transformer         =   tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            DOMSource src                   =   new DOMSource(doc);
            String versionPath              =   Config.getInstance().getVersionPath();
            StreamResult res                =   new StreamResult(new File(versionPath));
            transformer.transform(src, res);
        }
        
        catch(ParserConfigurationException | DOMException | TransformerException e)
        {
            throw new UpdateException(UpdateException.ErrorCode.REMOVE_PATCH_ERR, e);
        }
    }
}
