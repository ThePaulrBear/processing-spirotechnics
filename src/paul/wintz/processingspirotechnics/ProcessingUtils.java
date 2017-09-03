package paul.wintz.processingspirotechnics;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import gifAnimation.GifMaker;
import paul.wintz.spirotechnics.canvas.SaveType;
import processing.core.PApplet;
import processing.core.PGraphics;

public class ProcessingUtils {

	private static PApplet papplet;
	// DESKTOP INTERACTIONS (OPEN/CLOSE FILES)
	public final static Desktop DESKTOP = Desktop.getDesktop();

	@SuppressWarnings("static-access")
	public ProcessingUtils(PApplet pApplet) {
		ProcessingUtils.papplet = pApplet;
	}

	/**
	 * Maps a value from the domain specified into the given range.
	 *
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

	public static PGraphics createPGraphics(int width, int height) throws IllegalArgumentException {
		if (width < 1 || height < 1)
			throw new IllegalArgumentException("Size must be at least 1x1!");
		final PGraphics graphic = papplet.createGraphics(width, height);
		graphic.beginDraw();
		return graphic;
	}

	public static float randomGaussian() {
		return papplet.randomGaussian();
	}

	/**
	 * Create a new GifMaker. <br>
	 * Note that the size of the GIF is <yIntercept> not </yIntercept> set here.
	 *
	 * @param fileName
	 * @return
	 */
	public static GifMaker newGifMaker(File file) {
		// GifMaker(PApplet parent, String filename, int quality, int
		// transparentColor)
		return new GifMaker(papplet, file.getPath());
	}

	static void openFolder() {
		try {
			ProcessingUtils.DESKTOP.open(SaveType.getBaseFolder());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isInititialized() {
		return !(papplet == null);
	}

}
