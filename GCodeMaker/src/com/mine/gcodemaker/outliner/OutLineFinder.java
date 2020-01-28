//Actually finds not outline but area inside of it.
package com.mine.gcodemaker.outliner;
import com.mine.gcodemaker.importer.PixelPrimitive;
import java.util.*;
import java.lang.*;
public class OutLineFinder{
	public HashMap<PixelPrimitive, ArrayList<PixelPrimitive>> getAreaMap(){
		return buffs;
	}
	private ArrayDeque<Thread> threads = new ArrayDeque<>();
	private HashMap<PixelPrimitive, ArrayList<PixelPrimitive>> buffs = new HashMap<>();
	//private ArrayList<PixelPrimitive> keysforbuffs = new ArrayList<>();
	private PixelPrimitive pixels[][];
	private void runallthreads(){
		while(threads.peek()!=null){
			Thread now = threads.peek();
			threads.pop().start();
			try{
				now.join();
			}catch(InterruptedException ex){ex.printStackTrace();}
		}
		sortFoundedAreas();
	}
	private synchronized void fillwitharea(PixelPrimitive startp){
		if(startp==null || startp.getchecked()){
			System.out.println("ADDED NEW NULL FILLAREA");
			return;
		}
		System.out.println("ADDED NEW FILLAREA "+startp.getx()+" "+startp.gety());
		Stack<PixelPrimitive> buff = new Stack<>();//buffer for deletable pixels
		PixelPrimitive firststartpoint = startp;
		threads.add(new Thread(()->{
			if(!firststartpoint.getchecked()){
			System.out.println("Lambda goes here, anonymous was");
			PixelPrimitive startpoint = startp;//because for safety lambda uses only final or effictevly final
			buffs.put(startpoint, new ArrayList<PixelPrimitive>());
			buff.push(startpoint);
			PixelPrimitive nowpoint;
			PixelPrimitive initbuffer = null;
			while(true){
				startpoint.setchecked();
				nowpoint = startpoint;
				System.out.println("Startpoint is: "+"X = "+nowpoint.getx()+" Y = "+nowpoint.gety());
				while(true){
					buffs.get(firststartpoint).add(startpoint);
					if(!checkIfUpIsStrokeOrChecked(nowpoint)){
						nowpoint = pixels[nowpoint.getx()][nowpoint.gety()-1];
						buffs.get(firststartpoint).add(nowpoint);
						nowpoint.setchecked();
						nowpoint.setLeftStroke(checkIfLeftIsStrokeOrChecked(nowpoint));
				 		nowpoint.setRightStroke(checkIfRightIsStrokeOrChecked(nowpoint));
				 		System.out.println("Going Up: "+"X = "+nowpoint.getx()+" Y = "+nowpoint.gety());
				 		if(!nowpoint.getLeftStroke() || !nowpoint.getRightStroke()){
				 			while(addKeyPixeltoKeyBuffer(nowpoint)!=null){
				 				buff.push(addKeyPixeltoKeyBuffer(nowpoint)).setchecked();
				 				System.out.println("Up key "+buff.peek().getx()+" "+buff.peek().gety());
				 			}
				 		}		 		
					}
					else{
						initbuffer = findNextAreaStart(nowpoint,NextSides.UP);
						if(initbuffer!=null && !initbuffer.getnewarea()){
							initbuffer.setnewarea();
							fillwitharea(initbuffer);
						}
						break;
					}
				}
				nowpoint = startpoint;
				while(true){
					if(!checkIfDownIsStrokeOrChecked(nowpoint)){
						nowpoint = pixels[nowpoint.getx()][nowpoint.gety()+1];
						buffs.get(firststartpoint).add(nowpoint);
						nowpoint.setchecked();
						nowpoint.setLeftStroke(checkIfLeftIsStrokeOrChecked(nowpoint));
				 		nowpoint.setRightStroke(checkIfRightIsStrokeOrChecked(nowpoint));
				 		System.out.println("Going Down: "+"X = "+nowpoint.getx()+" Y = "+nowpoint.gety());
				 		if(!nowpoint.getLeftStroke() || !nowpoint.getRightStroke()){
				 			while(addKeyPixeltoKeyBuffer(nowpoint)!=null){
				 				buff.push(addKeyPixeltoKeyBuffer(nowpoint)).setchecked();
				 				System.out.println("Down key "+buff.peek().getx()+" "+buff.peek().gety());
				 			}
				 		}		
					}
					else{
						initbuffer = findNextAreaStart(nowpoint,NextSides.DOWN);
						if(initbuffer!=null && !initbuffer.getnewarea()){
							initbuffer.setnewarea();
							fillwitharea(initbuffer);
						}
						break;
					}
				}	
				if(!checkIfLeftIsStrokeOrChecked(startpoint)){
					startpoint = pixels[startpoint.getx()-1][startpoint.gety()];
					buff.push(startpoint);
					System.out.println("Flag 1: "+"X = "+startpoint.getx()+" Y = "+startpoint.gety());
				}else{
					initbuffer = findNextAreaStart(startpoint,NextSides.LEFT);
					if(initbuffer!=null && !initbuffer.getnewarea()){
							initbuffer.setnewarea();
							fillwitharea(initbuffer);
						}
					if(!checkIfRightIsStrokeOrChecked(startpoint)){
						startpoint = pixels[startpoint.getx()+1][startpoint.gety()];
						buff.push(startpoint);
						System.out.println("Flag 2: "+"X = "+startpoint.getx()+" Y = "+startpoint.gety());
					}else{
						initbuffer = findNextAreaStart(startpoint,NextSides.RIGHT);
						if(initbuffer!=null && !initbuffer.getnewarea()){
							initbuffer.setnewarea();
							fillwitharea(initbuffer);
						}
						if(!buff.empty()){
							startpoint = buff.pop();
							System.out.println("Flag 3: "+"X = "+startpoint.getx()+" Y = "+startpoint.gety());
						}else{
							break;
						}
					}
				}

			}
			for(int i = 0; i < pixels[0].length; i++){
				for(int j = 0; j < pixels.length; j++){
					if(pixels[j][i].getchecked()){
						System.out.print("#");
					}
					else{
						System.out.print(" ");
					}
				}
				System.out.println();
			}
		}}));
	}
	private enum NextSides{
		LEFT(-1), RIGHT(1), UP(-1), DOWN(1);
		private int counter;
		NextSides(int counter){
			this.counter = counter;
		}
		protected int getcounter(){return counter;}
	}
	private int xside;
	private int yside;
	private PixelPrimitive findNextAreaStart(PixelPrimitive nowpoint, NextSides side){
		xside = 0;
		yside = side.getcounter();
		if(side == NextSides.LEFT || side == NextSides.RIGHT){
			int xside = side.getcounter();
			int yside = 0;
		}
		if(!nowpoint.getBinary() && !nowpoint.getchecked()){
			return nowpoint;
		}
		else{
			try{
				return findNextAreaStart(pixels[nowpoint.getx()+xside][nowpoint.gety()+yside], side);
			}catch(IndexOutOfBoundsException ex){
				return null;
			}
		}
	}
	private PixelPrimitive addKeyPixeltoKeyBuffer(PixelPrimitive pixel){//because buff initializes inside of method we changed code
		if(!pixel.getLeftStroke() && !pixels[pixel.getx()-1][pixel.gety()].getchecked()){
			return pixels[pixel.getx()-1][pixel.gety()];
		}
		if(!pixel.getRightStroke() && !pixels[pixel.getx()+1][pixel.gety()].getchecked()){
			return pixels[pixel.getx()+1][pixel.gety()];
		}
		System.out.println("No Key Pixels For "+"X = "+pixel.getx()+" Y = "+pixel.gety());
		return null;
	}
	private boolean checkIfUpIsStrokeOrChecked(PixelPrimitive nowpixel){
		if(nowpixel.gety()-1 >= 0 && !pixels[nowpixel.getx()][nowpixel.gety()-1].getchecked()){
			return pixels[nowpixel.getx()][nowpixel.gety()-1].getBinary();
		}
		else{
			return true;
		}
	}
	private boolean checkIfDownIsStrokeOrChecked(PixelPrimitive nowpixel){
		if(nowpixel.gety()+1 < pixels[0].length && !pixels[nowpixel.getx()][nowpixel.gety()+1].getchecked()){
			return pixels[nowpixel.getx()][nowpixel.gety()+1].getBinary();
		}
		else{
			return true;
		}
	}
	private boolean checkIfLeftIsStrokeOrChecked(PixelPrimitive nowpixel){
		if(nowpixel.getx()-1 >= 0 && !pixels[nowpixel.getx()-1][nowpixel.gety()].getchecked()){
			return pixels[nowpixel.getx()-1][nowpixel.gety()].getBinary();
		}
		else{
			return true;
		}
	}
	private boolean checkIfRightIsStrokeOrChecked(PixelPrimitive nowpixel){
		if(nowpixel.getx()+1 < pixels.length && !pixels[nowpixel.getx()+1][nowpixel.gety()].getchecked()){
			return pixels[nowpixel.getx()+1][nowpixel.gety()].getBinary();
		}
		else{
			return true;
		}
	}
	private void sortFoundedAreas(){//sorts with comparator

		ArrayList<PixelPrimitive> keys = new ArrayList<>(buffs.keySet()); 
		for(PixelPrimitive key:keys){
			Collections.sort(buffs.get(key),new Comparator<PixelPrimitive>(){
		public int compare(PixelPrimitive p1, PixelPrimitive p2){
			if(alreadysorted){
				if(p1.gety()==p2.gety()){
					return p1.getx()-p2.getx();
				}else{return 0;}
			}else{return p1.getx()-p2.getx();}
		}
		boolean alreadysorted;
		public Comparator<PixelPrimitive> setsorted(boolean alreadysorted){
			this.alreadysorted = alreadysorted;
			return this;
		}
	}.setsorted(false));

			Collections.sort(buffs.get(key),new Comparator<PixelPrimitive>(){
		public int compare(PixelPrimitive p1, PixelPrimitive p2){
			if(alreadysorted){
				if(p1.getx()==p2.getx()){
					return p1.gety()-p2.gety();
				}else{return 0;}
			}else{return p1.gety()-p2.gety();}
		}
		boolean alreadysorted;
		public Comparator<PixelPrimitive> setsorted(boolean alreadysorted){
			this.alreadysorted = alreadysorted;
			return this;
		}
	}.setsorted(true));
			
			System.out.println();
			System.out.println(key.getx()+" "+key.gety()+" KEY HERE");
			System.out.println();
			for(PixelPrimitive pixel : buffs.get(key))
				System.out.println(pixel.getx()+" "+pixel.gety());
		}

	}
	public OutLineFinder(PixelPrimitive[][] pixels){
		this.pixels = pixels;
		PixelPrimitive startpixel = pixels[11][10];
		fillwitharea(startpixel);
		runallthreads();	

	}
		
}