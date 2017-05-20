package paul.wintz.processingspirotechnics;

import java.awt.Desktop;
import java.io.IOException;

import gifAnimation.GifMaker;
import paul.wintz.spirotechnics.canvas.SaveType;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class ProcessingUtils {

	private static PApplet papplet;
	// DESKTOP INTERACTIONS (OPEN/CLOSE FILES)
	public final static Desktop DESKTOP = Desktop.getDesktop();

	@SuppressWarnings("static-access")
	public ProcessingUtils(PApplet pApplet){
		ProcessingUtils.papplet = pApplet;
	}
	
	/**
	 * Maps a value from the domain specified into the given range. 
	 * @param value
	 * @param startDomain
	 * @param stopDomain
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static float map(double value, float startDomain, float stopDomain, float startRange, float stopRange) {
		return papplet.map((float) value, startDomain, stopDomain, startRange, stopRange);
	}

	public static PGraphics createGraphics(int i, int j) throws IllegalArgumentException {
		if(i < 1 || j < 1) throw new IllegalArgumentException("Size must be at least 1x1!");
		PGraphics graphic = papplet.createGraphics(i,j);
		graphic.background(0);
		graphic.clear();
		graphic.loadPixels();
		if(graphic.pixels == null) throw new IllegalStateException("The graphic was not created!");
//		if(!graphic.isLoaded()) throw new IllegalStateException("The graphic was not created!");
		return graphic;
	}

	public static float randomGaussian(){
		return papplet.randomGaussian();
	}

	/**
	 * Create a new GifMaker. <br>
	 * Note that the size of the GIF is <b> not </b> set here.
	 * @param fileName
	 * @return
	 */
	public static GifMaker newGifMaker(String fileName) {
		//GifMaker(PApplet parent, String filename, int quality, int transparentColor)
		return new GifMaker(papplet, fileName);
	}

	static void openFolder() {
		try {
			ProcessingUtils.DESKTOP.open(SaveType.getBaseFolder());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
