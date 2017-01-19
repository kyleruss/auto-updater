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
import javax.swing.JOptionPane;

public class ErrorLogger 
{
    private static String logFile;
    static
    {
        
    }
    
     //Commits a log messsage from logger
    //Log is only written if logging is enabled
    public static void log(String message, String logger_name)
    {
        Logger current       =   null;
        
        try
        {
            //Get loggers and handler
            logger       =   create(logger_name);
            handler      =   logger.getHandler();
            current      =   Logger.getLogger(logger_name);

            if(current == null || handler == null) throw new IOException();
            else
            {

                //set logger params and attach handler
                current.setLevel(Level.FINE);
                current.setUseParentHandlers(false);
                current.addHandler(handler);

                //Write log with message
                current.fine(message);

            }
        }

        catch(IOException | SecurityException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to commit log: " + e.getMessage());
        }

        //flush and close handlers
        finally
        {
            if(handler != null && current != null)
            {
                handler.flush();
                handler.close();
            }
        }
    }
    
    public Handler getHandler() throws IOException, SecurityException
    {
        Handler fh =  new FileHandler
        (
                log_file, //Log file name
                Config.LOG_MAX_SIZE, //Log max file size
                Config.LOG_FILE_MAX_COUNT, //Max number of logs created after max size
                true //Allow appending to existing logs
        );
        
        //SimpleFormatter should be used over XMLFormatter
        fh.setFormatter(log_formatter);
        return fh;
    }
    
    //Returns the correct formatting including path of log file
    //Template: /FOLDER/logname_dd-mm-yyy
    public static String formatLogName(String file)
    {
        final char delimiter            =   '_';
        final String folder             =   Config.LOG_PATH;
        Date time                       =   new Date();
        SimpleDateFormat date_format    =   new SimpleDateFormat("dd-MM-yyyy");
        String date                     =   date_format.format(time);
        
        String formatted_file   =   folder + file + delimiter + date;
        return formatted_file;
    }   
}
