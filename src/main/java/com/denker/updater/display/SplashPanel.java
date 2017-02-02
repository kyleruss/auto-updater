//--------------------------------------
//  Kyle Russell
//  Auto-Updater
//  github.com/denkers/auto-updater
//--------------------------------------

package com.denker.updater.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SplashPanel extends JPanel
{
    private BufferedImage splashImage;
    
    public SplashPanel()
    {
        setPreferredSize(new Dimension(466, 151));
        setBackground(new Color(255, 255, 255, 0));
        initImageContent();
    }
    
    private void initImageContent()
    {
        try
        {
            splashImage =   ImageIO.read(new File("data/images/splash.png"));
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to load image contents");
        }
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(splashImage, 0, 0, null);
    }
}
