package paul.wintz.processingspirotechnics;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.Desktop;
import java.io.*;

import gifAnimation.GifMaker;
import paul.wintz.logging.Lg;
import paul.wintz.spirotechnics.canvas.SaveType;
import processing.core.*;

public final class ProcessingUtils {

	private static final String TAG = Lg.makeTAG(ProcessingUtils.class);
	private static PApplet papplet;
	public static Desktop DESKTOP = Desktop.getDesktop();

	public static void initialize(PApplet pApplet) {
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
	public static float map(double value, float startDomain, float stopDomain, float startRange, float stopRange) {
		return PApplet.map((float) value, startDomain, stopDomain, startRange, stopRange);
	}

	public static PGraphics createPGraphics(int width, int height) {
		checkArgument(width  > 0, "Width must be positive");
		checkArgument(height > 0, "Height must be positive");

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
			Lg.e(TAG, "Failed to open folder", e);
		}
	}

	public static boolean isInititialized() {
		return papplet != null;
	}

	private ProcessingUtils() {}
}
