package com.mine.gcodemaker.importer;
import java.lang.*;
import java.util.*;
public class PixelPrimitive{
	private int xposition;
	private int yposition;
	private int zposition;
	private boolean fullprocessed;
	private boolean halfprocessed;//board of instrument
	private int red;
	private int green;
	private int blue;
	private boolean binar;
	private boolean avaliablenewarea;//true if new area available
	private boolean checked;//if already been here
	public ArrayList<PixelPrimitive> freeAround;
	private boolean strokeleft;
	private boolean strokeright;
	public int getR(){
		return red;
	}
	public int getG(){
		return green;
	}
	public int getB(){
		return blue;
	}
	public boolean getBinary(){
		return binar;
	}
	private boolean binarize(){
		boolean b =false;
		if(blue == red)
			b = true;
		if((red + green + blue < 10)||((red == green == b) && (red < 150) && (green < 150) && (blue < 150))||((Math.abs(red - green) < 10) && (Math.abs(red - blue) < 10) && (Math.abs(green - blue) < 10) && red< 150)){
			//return true if color is nearby to black or if it`s grey. Reurns false if any other. It`s just a simple binarization algorithm which works only if stroke is black or gray.
			return true;
		}
		else{
			return false;
		}
	}
	public boolean getchecked(){
		return checked;
	}
	public void setchecked(){
		this.checked = true;
	}
	public boolean getnewarea(){
		return avaliablenewarea;
	}
	public void setnewarea(){
		this.avaliablenewarea = true;
	}
	public void setx(int x){
		this.xposition = x;
	}
	public void sety(int y){
		this.yposition = y;
	}
	public void setz(int z){
		this.zposition = z;
	}
	public int getx(){
		try{
			return xposition;
		}catch(Exception ex){ex.printStackTrace();return -1;}
	}
	public int gety(){
		try{
			return yposition;
		}catch(Exception ex){ex.printStackTrace();return -1;}
	}
	public int getz(){
		try{
			return zposition;
		}catch(Exception ex){ex.printStackTrace();return -1;}
	}
	public void setLeftStroke(boolean b){//stroke or checked
		this.strokeleft = b;
	}
	public void setRightStroke(boolean b){
		this.strokeright = b;
	}
	public boolean getLeftStroke(){
		return strokeleft;
	}
	public boolean getRightStroke(){
		return strokeright;
	}

	
	public void setfullprocessed(boolean b){
		this.fullprocessed = b;
	}
	public boolean getfullprocessed(){
		return fullprocessed;
	}
	public void sethalfprocessed(boolean b){
		this.fullprocessed = b;
	}
	public boolean gethalfprocessed(){
		return fullprocessed;
	}


	protected PixelPrimitive(int r, int g, int b){
		this.red = r;
		this.green = g;
		this.blue = b;
		this.avaliablenewarea = false;
		this.checked = false;
		this.strokeleft = false;
		this.strokeright = false;
		this.fullprocessed = false;
		this.halfprocessed = false;
		this.binar = this.binarize();
	}
}