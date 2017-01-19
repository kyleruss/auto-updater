//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.io;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;

public class ErrorLogger 
{
    private static String logFile;
    private static final String LOGGER_NAME =   "error_logger";
    
    static
    {
        
    }
    
     //Commits a log messsage from logger
    //Log is only written if logging is enabled
    public static void log(String message)
    {
        Handler handler =   null;
        Logger logger   =   null;
        try
        {
            //Get loggers and handler
            handler =   getHandler(logFile);
            logger   =   Logger.getLogger(LOGGER_NAME);
            
            if(handler == null) throw new IOException();
            else
            {

                //set logger params and attach handler
                logger.setLevel(Level.FINE);
                logger.setUseParentHandlers(false);
                logger.addHandler(handler);

                //Write log with message
                logger.fine(message);

            }
        }

        catch(IOException | SecurityException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to commit log: " + e.getMessage());
        }

        if(handler != null && logger != null)
        {
            handler.flush();
            handler.close();
        }
    }
    
    public static Handler getHandler(String logName) throws IOException, SecurityException
    {
        FTPConfig conf  =   FTPConfig.getInstance();
        
        Handler fh =  new FileHandler
        (
                logName, //Log file name
                conf.getMaxLogSize(), //Log max file size
                conf.getMaxLogCount(), //Max number of logs created after max size
                true //Allow appending to existing logs
        );
        
        //SimpleFormatter should be used over XMLFormatter
        fh.setFormatter(new SimpleFormatter());
        return fh;
    }
    
    //Returns the correct formatting including path of log file
    //Template: /FOLDER/logname_dd-mm-yyy
    public static String formatLogName(String file)
    {
        FTPConfig conf                  =   FTPConfig.getInstance();
        final char delimiter            =   '_';
        final String folder             =   conf.getLogDir();
        Date time                       =   new Date();
        SimpleDateFormat date_format    =   new SimpleDateFormat("dd-MM-yyyy");
        String date                     =   date_format.format(time);
        
        String formatted_file   =   folder + file + delimiter + date;
        return formatted_file;
    }   
}
