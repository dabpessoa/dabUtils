package me.dabpessoa.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtils {

	public static Image resize(String caminho ,int w, int h){
		BufferedImage fundo = null;  
		try { fundo = ImageIO.read(new File(caminho)); }   
		catch (IOException e1) { fundo = new BufferedImage(1,1,BufferedImage.BITMASK ); }  
		return fundo.getScaledInstance(w, h, 10000);  
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
    public static ImageIcon createImageIcon(Object clazz, String path,
                                               String description) {
        java.net.URL imgURL = clazz.getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Não foi possível localizar o arquivo: " + path);
            return null;
        }
    }
	
}
