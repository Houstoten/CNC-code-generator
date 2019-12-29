package com.mine.gcodemaker.outliner;
import com.mine.gcodemaker.importer.PixelPrimitive;
import com.mine.gcodemaker.outliner.AreaThread;
import java.util.*;
import java.lang.*;
public class OutLineFinder{
	private int startPix = 0;
	private int countlvl = 0;
	private ArrayList<AreaThread> threads;
	private boolean[][] binarycopy;//copy of binary colored img to work with
	private void runallthreads(){
		for(AreaThread thread:threads)
			thread.run();
	}
	private boolean[][] makeacopy(PixelPrimitive[][] pixels){//copies img binary data to new array
		boolean[][] buff = new boolean[pixels.length][pixels[0].length];
		for(int i=0; i<pixels.length;i++){
			for(int j=0; j<pixels[i].length;j++){
				buff[i][j]=pixels[i][j].getBinary();
			}
		}
		return buff;
	}
	private void fillwitharea(PixelPrimitive[][] pixels){
		threads = new ArrayList<>();
		ArrayList<PixelPrimitive> buff = new ArrayList<>();
		threads.add(new AreaThread(new Thread(()->{
			System.out.println("Lambda goes here");
			for(int i=0; i<binarycopy.length;i++){
				for(int j=0; j<binarycopy[i].length;j++){
					pixels[i][j].setLeftStroke(checkIfLeftIsStroke(i,j));
					pixels[i][j].setRightStroke(checkIfRightIsStroke(i,j));
					if(pixels[i][j].getchecked()==false){
						pixels[i][j].setchecked();
						if(!binarycopy[i][j]){
							buff.add(pixels[i][j]);
						}
						else{
							pixels[i][j].setoutline();
							break;
						}
					}
					System.out.println("RGB of "+i+" "+j+" is "+ pixels[i][j].getR()+" "+pixels[i][j].getG()+" "+pixels[i][j].getB()+" binary is "+pixels[i][j].getBinary());
				}
			}
		}), countlvl));
		threads.get(countlvl).pixin.addAll(buff);
		buff.clear();
		countlvl++;
		runallthreads();
	}
	// private PixelPrimitive backtrack(){
	// 	//stopped here

	// }
	private boolean checkIfLeftIsStroke(int iposition, int jposition){
		if(iposition-1 >= 0){
			return binarycopy[iposition-1][jposition];
		}
		else{
			return true;
		}
	}
	private boolean checkIfRightIsStroke(int iposition, int jposition){
		if(iposition+1 < binarycopy.length){
			return binarycopy[iposition+1][jposition];
		}
		else{
			return true;
		}
	}

	public OutLineFinder(PixelPrimitive[][] pixels){
		fillwitharea(pixels);
		binarycopy = makeacopy(pixels);
	}
}