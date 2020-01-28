package com.mine.gcodemaker.codegen;
import com.mine.gcodemaker.importer.PixelPrimitive;
import java.io.*;
import java.util.*;
import java.lang.*;
public class CodeGenerator{
	private HashMap<PixelPrimitive, ArrayList<PixelPrimitive>> areasMap;
	private PixelPrimitive[][] pixels;
	private Stack<Double> diams;
	private HashMap<Double, ArrayList<PixelPrimitive>> diamsMap = new HashMap<>();
	private double depth;
	private double scale;
	public void setmeasures(double depth, double scale, double mainD, double ... d){
		this.scale = scale;
		this.depth = depth;
		diams = new Stack<>();//tools` diameters stack
		if(d.length!=0){
			double[] sorteddiams = heapsort(mainD, d);
			for(Double diam:sorteddiams){
				System.out.print(diam+" ");
				diams.push(diam*scale);
			}
		}else{
			diams.push(mainD*scale);
		}
	}
	public void generate(){
		//working with diams
		while(diams.peek()!=null){
			for(double i = diams.peek()/2; i < pixels[0].length-diams.peek()/2; i++){
				for(double j = diams.peek()/2; j < pixels.length-diams.peek()/2; j++){
					try{
						diamsMap.get(diams.peek()).addAll(checkcircle(pixels[(int)i][(int)j], i - (int)i, j - (int)j));//lossy conversion fix done
					}catch(Exception ex){System.out.println(ex.getMessage());}
				}
			}
			diams.pop();
		}
	}
	private ArrayList<PixelPrimitive> checkcircle(PixelPrimitive centerpixel, double savedx, double savedy){
		ArrayList<PixelPrimitive> nowlist = new ArrayList<>();
		PixelPrimitive firstpixel = pixels[(int)(centerpixel.getx()+savedx-diams.peek()/2)][centerpixel.gety()];
		PixelPrimitive lastpixel = pixels[(int)(centerpixel.getx()+savedx+diams.peek()/2)][centerpixel.gety()];
		boolean firstlastpoint = false;
		for(int i = firstpixel.getx(); i < lastpixel.getx(); i++){
			for(int j = centerpixel.gety() - findheightforcicle(i,centerpixel.getx())/2; j < centerpixel.gety() + findheightforcicle(i,centerpixel.getx())/2; j++){
				if(j == centerpixel.gety() - findheightforcicle(i,centerpixel.getx())/2 || j == centerpixel.gety() + findheightforcicle(i,centerpixel.getx())/2)
					firstlastpoint = true;
				if(!pixels[i][j].getBinary() && !pixels[i][j].getfullprocessed()){
					if(!firstlastpoint){
						pixels[i][j].setfullprocessed(true);
					}else{
						pixels[i][j].sethalfprocessed(true);
						firstlastpoint = false;
					}
					if(pixels[i][j].gethalfprocessed() && )//if already halfprocessed
					nowlist.add(pixels[i][j]);
				}else{
					for(PixelPrimitive pixel:nowlist){
						pixel.setfullprocessed(false);
						pixel.sethalfprocessed(false);//last notes could be erased!!!
					}
					nowlist.clear();
					return nowlist;
				}
			}		
		}
		return nowlist;
	}
	private void randomizeLevels(){

	}
	private int findheightforcicle(int x, int centerx){
		return (int)Math.sqrt(diams.peek()*diams.peek() - (2*(centerx - x))*(2*(centerx - x)));
	}
	//Here sort needed. Sort block start
	private double[] heapsort(double d, double oldarr[])
    {
    	double[] arr = Arrays.copyOf(oldarr, oldarr.length+1);
    	arr[arr.length-1] = d;
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
          heapify(arr, n, i);
        }
        for (int i = n - 1; i >= 0; i--)
        {
            double temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            heapify(arr, i, 0);
        }
        return arr;
    }
    private void heapify(double arr[], int n, int i)
    {
        int largest = i; 
        int l = 2*i + 1; 
        int r = 2*i + 2;  
        if (l < n && arr[l] > arr[largest])
            largest = l;
        if (r < n && arr[r] > arr[largest])
            largest = r;
        if (largest != i)
        {
            double swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, n, largest);
        }
    }
//Sort block end
	public CodeGenerator(HashMap<PixelPrimitive, ArrayList<PixelPrimitive>> areasMap, PixelPrimitive[][] pixels){
		this.areasMap = areasMap;
		this.pixels = pixels;
	}

}