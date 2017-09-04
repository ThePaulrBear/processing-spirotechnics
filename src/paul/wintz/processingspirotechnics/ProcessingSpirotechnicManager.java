package paul.wintz.processingspirotechnics;

import static paul.wintz.spirotechnics.InitialValues.SIDEBAR_WIDTH;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import paul.wintz.canvas.Layer;
import paul.wintz.spirotechnics.*;
import paul.wintz.spirotechnics.userinterface.swinggui.OptionsJFrame;
import paul.wintz.userinterface.HasValue;
import processing.core.*;

public class ProcessingSpirotechnicManager extends SpirotechnicManager<PGraphics> {
	private final PApplet pApplet;
	OptionsJFrame frame;

	public ProcessingSpirotechnicManager(final PApplet pApplet) {
		super(
				new PGraphicsLayerFactory(),
				new MyDisplayer(pApplet),
				new MyLayerCompositor(),
				new MyToaster(pApplet),
				new MyLogger(),
				new ProcessingGifRecording());
		this.pApplet = pApplet;

		openOptionsWindow();
	}

	private void openOptionsWindow() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					frame = new OptionsJFrame(getUserInterface().getOptionsTree());
					frame.setVisible(true);
					frame.addWindowListener(new java.awt.event.WindowAdapter() {
						@Override
						public void windowClosing(java.awt.event.WindowEvent windowEvent) {
							pApplet.exit();
						}
					});
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void close() {
		super.close();
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	int framesSinceKeyPress = Integer.MAX_VALUE / 2;

	private static final class PGraphicsLayerFactory implements LayerFactory<PGraphics> {
		@Override
		public Layer<PGraphics> makeLayer(int width, int height) {
			return new PGraphicsCanvas(width, width);
		}
	}

	private static final class MyLayerCompositor implements LayersCompsitor<PGraphics> {

		final PGraphics base = ProcessingUtils.createPGraphics(1, 1);

		@Override
		public PGraphics createCompositeImage(List<Layer<PGraphics>> list) {

			base.setSize(list.get(0).getWidth(), list.get(0).getHeight());
			base.beginDraw();
			for (final Layer<PGraphics> layer : list) {
				layer.drawOnto(base);
			}
			base.endDraw();
			return base;
		}
	}

	/**
	 * Draw text to the PApplet that describes the most recent UI action. The
	 * text fades away over a set number of frames.
	 *
	 * @param pApplet
	 */
	private final static class MyToaster extends SpirotechnicManager.ToastCallback {
		private final PApplet pApplet;

		public MyToaster(PApplet pApplet) {
			this.pApplet = pApplet;
		}

		@Override
		protected void onDrawToast() {
			framesSinceKeyPress++;

			pApplet.textAlign(PConstants.CENTER, PConstants.BOTTOM);
			pApplet.textSize(24);
			final int alpha = 255 - 3 * framesSinceKeyPress; // TODO: Make these
			// calculations
			// more
			// structured
			final int x = pApplet.width / 2;
			final int y = pApplet.height - 30;

			// draw Shadow
			pApplet.fill(0, alpha);
			pApplet.text(message, x + 2, y + 2);

			// drawText
			pApplet.fill(255, alpha);
			pApplet.text(message, x, y);
		}
	}

	final static class MyLogger implements SpirotechnicManager.LogCallback {
		public MyLogger() {
		}

		@Override
		public void logStd(String tag, String message) {
			System.out.println(message);
		}

		@Override
		public void logError(String tag, String message) {
			System.err.println(message);
		}

		@Override
		public void logError(String tag, String message, Exception e) {
			e.printStackTrace();
		}

	}

	final static class MyDisplayer implements SpirotechnicManager.ImageDisplayer<PGraphics> {
		private final PApplet pApplet;

		public MyDisplayer(PApplet pApplet) {
			this.pApplet = pApplet;
		}

		@Override
		public void onDisplay(PGraphics image) {
			final int shortestEdge = Math.min(pApplet.height, pApplet.width - SIDEBAR_WIDTH);
			final float displayScale = (float) shortestEdge / (float) image.width;

			pApplet.scale(displayScale, displayScale);
			pApplet.image(image, SIDEBAR_WIDTH / displayScale, 0);
			pApplet.scale(1 / displayScale, 1 / displayScale);
		}
	}

	/**
	 * Using this library: https://github.com/01010101/GifAnimation
	 *
	 * @author PaulWintz
	 *
	 */
	private static class ProcessingGifRecording implements GifRecorder<PGraphics> {
		private gifAnimation.GifMaker gifMaker;
		private File file;
		private HasValue<Integer> fpsOption;
		private int framesRecorded = 0;
		private boolean isOpen = false;

		private void printMaxFramesToBeUnderGivenFileSize(long maxSize) {
			final double sizePerFrame = (double) file.length() / (double) framesRecorded;
			final double maxNumber = maxSize / sizePerFrame;
			System.out.println("The maximum number of frames is: " + maxNumber);
		}

		@Override
		public void open(File file) {
			this.file = file;
			System.out.println("Beginning GIF record to: " + file.getPath());
			framesRecorded = 0;
			gifMaker = ProcessingUtils.newGifMaker(file);
			gifMaker.setRepeat(0);

			isOpen = true;
		}

		@Override
		public void addFrame(PGraphics frame) {
			if (!isOpen)
				throw new IllegalStateException("The GIF is not open");

			final int frameDelay = (int) (1000.0 / (double) fpsOption.getValue());
			gifMaker.setDelay(frameDelay);
			gifMaker.addFrame(frame);
			framesRecorded++;
		}

		@Override
		public int getFrameCount() {
			if (isOpen)
				return framesRecorded;
			else
				return -1;
		}

		@Override
		public void close() throws IllegalStateException {
			if (!isOpen)
				throw new IllegalStateException("The GIF is not open");

			isOpen = false;

			gifMaker.finish();
			System.out.println("GIF closed. Number of frames: " + framesRecorded);

			printMaxFramesToBeUnderGivenFileSize((long) 3e6);

			gifMaker = null;
			file = null;
		}

		@Override
		public void setFPSOption(HasValue<Integer> fpsOption) {
			this.fpsOption = fpsOption;
		}
	}


}
