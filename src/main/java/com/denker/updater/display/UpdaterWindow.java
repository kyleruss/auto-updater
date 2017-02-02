//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.display;

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
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
    
    public void display()
    {
        if(frame != null)
            frame.setVisible(true);
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
