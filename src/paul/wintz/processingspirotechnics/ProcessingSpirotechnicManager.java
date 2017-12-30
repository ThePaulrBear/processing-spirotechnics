package paul.wintz.processingspirotechnics;

import static com.google.common.base.Preconditions.checkState;
import static paul.wintz.parametricequationdrawer.InitialValues.SIDEBAR_WIDTH;
import static paul.wintz.utils.Utils.checkPositive;
import static paul.wintz.utils.logging.Lg.makeTAG;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import paul.wintz.canvas.*;
import paul.wintz.parametricequationdrawer.*;
import paul.wintz.processing.*;
import paul.wintz.spirotechnics.userinterface.swinggui.OptionsJFrame;
import paul.wintz.utils.logging.Lg;
import processing.core.*;

public class ProcessingSpirotechnicManager extends SpirotechnicMain<PGraphics> {
	private static final String TAG = makeTAG(ProcessingSpirotechnicManager.class);
	private final PApplet pApplet;
	private OptionsJFrame frame;

	public ProcessingSpirotechnicManager(final PApplet pApplet) {
		super(
				new PGraphicsLayerFactory(),
				new PGraphicsToPAppletDisplayer(pApplet),
				new MyLayerCompositor(),
				new ProcessingGifRecording());
		this.pApplet = pApplet;

		openOptionsWindow();
	}

	private void openOptionsWindow() {

		EventQueue.invokeLater(() -> {

			try {
				frame = new OptionsJFrame();
				frame.addTab(getUserInterface().getDrawerController());
				frame.addTab(getUserInterface().getAnimationController());
				frame.addTab(getUserInterface().getCanvasController());

				frame.setVisible(true);
				frame.addWindowListener(new java.awt.event.WindowAdapter() {

					@Override
					public void windowClosing(java.awt.event.WindowEvent windowEvent) {
						pApplet.exit();
					}
				});

			} catch (final Exception e) {
				Lg.e(TAG, "Failed to create options window", e);
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
			return new PGraphicsLayer(width, width);
		}
	}

	private static final class MyLayerCompositor implements ImageCompositor<PGraphics> {

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

	private static final class PGraphicsToPAppletDisplayer implements SpirotechnicMain.ImageDisplayer<PGraphics> {
		private final PApplet pApplet;

		public PGraphicsToPAppletDisplayer(PApplet pApplet) {
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
	private static class ProcessingGifRecording implements AnimationSaver<PGraphics> {
		private static final String TAG = makeTAG(ProcessingGifRecording.class);

		private gifAnimation.GifMaker gifMaker;
		private File file;
		private float fps;
		private int framesRecorded = 0;
		private boolean isOpen = false;

		private void printMaxFramesToBeUnderGivenFileSize(long maxSize) {
			final double sizePerFrame = (double) file.length() / (double) framesRecorded;
			final double maxNumber = maxSize / sizePerFrame;
			Lg.i(TAG, "The maximum number of frames is: " + maxNumber);
		}

		@Override
		public void open(File file) {
			this.file = file;
			Lg.i(TAG, "Beginning GIF record to: " + file.getAbsolutePath());
			framesRecorded = 0;
			gifMaker = ProcessingUtils.newGifMaker(file);
			gifMaker.setRepeat(0);

			isOpen = true;
		}

		@Override
		public void addFrame(PGraphics frame) {
			checkState(isOpen, "The GIF is not open");

			final int frameDelayInMillis = (int) (1000.0f / fps);
			Lg.d(TAG, "delay: " + frameDelayInMillis);
			gifMaker.setDelay(frameDelayInMillis);
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
		public void close() {
			checkState(isOpen, "The GIF is not open");

			isOpen = false;

			gifMaker.finish();
			Lg.i(TAG, "GIF closed. Number of frames: " + framesRecorded);

			printMaxFramesToBeUnderGivenFileSize((long) 3e6);

			gifMaker = null;
			file = null;
		}

		@Override
		public void setFPS(float fps) {
			checkPositive(fps);
			Lg.i(TAG, "fps set to " + fps);
			this.fps = fps;
		}
	}


}
