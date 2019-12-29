package com.mine.gcodemaker.importer;
import com.mine.gcodemaker.importer.PixelPrimitive;
import java.io.*;
import java.lang.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class ImageImporter{
	private BufferedImage imgfile = null;
	public static PixelPrimitive[][] pixels; 
	public ImageImporter(String imgpath){
		try{
			this.imgfile = ImageIO.read(new File(imgpath));
		}
		catch(IOException e){
			System.out.println("Check img file in directory");
		}
		if(imgfile!=null){
			pixels = new PixelPrimitive[imgfile.getWidth()][imgfile.getHeight()];
			for(int i = 0; i < pixels.length; i++){
				for(int j = 0; j < pixels[i].length; j++){
					Color c = new Color(imgfile.getRGB(i,j));
					pixels[i][j] = new PixelPrimitive(c.getRed(), c.getGreen(), c.getBlue());
				}
			}
		}	
	}
}