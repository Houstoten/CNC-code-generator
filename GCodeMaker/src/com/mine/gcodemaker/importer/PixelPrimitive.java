package com.mine.gcodemaker.importer;
import java.lang.*;
import java.util.*;
public class PixelPrimitive{
	private int xposition;
	private int yposition;
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
	protected PixelPrimitive(int r, int g, int b){
		this.red = r;
		this.green = g;
		this.blue = b;
		this.avaliablenewarea = false;
		this.checked = false;
		this.strokeleft = false;
		this.strokeright = false;
		this.binar = this.binarize();
	}
}