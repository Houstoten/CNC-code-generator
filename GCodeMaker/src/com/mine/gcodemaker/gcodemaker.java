package com.mine.gcodemaker;
import com.mine.gcodemaker.importer.*;
import com.mine.gcodemaker.outliner.OutLineFinder;
import com.mine.gcodemaker.codegen.CodeGenerator;
import java.util.*;
public class gcodemaker{
	public static void main(String[] args){
		System.out.println("Testable hi");
		ImageImporter kk = new ImageImporter("res/imgres/Net_original.png");
		OutLineFinder finder = new OutLineFinder(ImageImporter.pixels);
		System.out.println(finder.getAreaMap().size()+" Areas Total");
		CodeGenerator generator = new CodeGenerator(finder.getAreaMap(), ImageImporter.pixels);
		generator.setmeasures(20, 0.5, 0.25, 5.5, 3.175);//size in mm. Set scale 1 for no changes
		generator.generate();
		System.out.println();
		System.out.println("Img size "+ImageImporter.pixels.length+" "+ImageImporter.pixels[0].length);
	}
}