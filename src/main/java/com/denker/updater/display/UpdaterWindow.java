//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.display;

import java.awt.Color;
import javax.swing.JFrame;

public class UpdaterWindow 
{
    private static UpdaterWindow instance;
    private JFrame frame;
    private UpdaterPanel panel;
    
    private UpdaterWindow() 
    {
        initDisplay();
    }
    
    private void initDisplay()
    {
        frame   =   new JFrame();
        panel   =   UpdaterPanel.getInstance();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.setUndecorated(true);
        frame.setBackground(new Color(255, 255, 255, 0));
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
    
    public void display()
    {
        if(frame != null)
        {
            frame.setVisible(true);
            panel.getStatusPanel().startUpdater();
        }
    }
    
    public static UpdaterWindow getInstance()
    {
        if(instance == null) instance = new UpdaterWindow();
        return instance;
    }
    
    public static void main(String[] args)
    {
        UpdaterWindow window    =   UpdaterWindow.getInstance();
        window.display();
    }
}
