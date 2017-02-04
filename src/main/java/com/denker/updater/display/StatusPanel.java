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
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class StatusPanel extends JPanel
{
    private BufferedImage panelImage;
    private JLabel statusIconLabel;
    
    public StatusPanel()
    {
        setBackground(new Color(255, 255, 255, 0));
        setPreferredSize(new Dimension(449, 75));
        initImageContent();
        
        statusIconLabel =   new JLabel(new ImageIcon("data/images/spinner.gif"));
        add(statusIconLabel);
    }
    
    private void initImageContent()
    {
        try
        {
            panelImage =   ImageIO.read(new File("data/images/bottom_panel.png"));
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
        g.drawImage(panelImage, 0, 0, null);
    }
}
