package com.mine.gcodemaker.outliner;
import java.lang.*;
import java.util.*;
import com.mine.gcodemaker.importer.PixelPrimitive;
public class AreaThread{
	private int level;
	private Thread thread;
	public ArrayList<PixelPrimitive> pixin;
	public AreaThread(Thread t, int lvl){
		this.level = lvl;
		this.thread = t;
		pixin = new ArrayList<>();
		System.out.println(level+" lvl ");
	}

	public void run(){
		thread.start();
	}
}