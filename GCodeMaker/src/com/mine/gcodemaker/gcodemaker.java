package com.mine.gcodemaker;
import com.mine.gcodemaker.importer.*;
import com.mine.gcodemaker.outliner.OutLineFinder;
import java.util.*;
public class gcodemaker{
	public static void main(String[] args) {
		System.out.println("Testable hi");
		ImageImporter kk = new ImageImporter("res/imgres/Net_original.png");
		OutLineFinder finder = new OutLineFinder(ImageImporter.pixels);
		// System.out.println(ImageImporter.pixels[0][0].getR()+" "+ImageImporter.pixels[0][0].getG()+" "+ImageImporter.pixels[0][0].getB());
		System.out.println("Img size "+ImageImporter.pixels.length+" "+ImageImporter.pixels[0].length);
	}
}