package paul.wintz.processingspirotechnics;

import paul.wintz.parametricequationdrawer.*;
import paul.wintz.processing.ProcessingUtils;
import paul.wintz.utils.Toast;
import paul.wintz.utils.logging.*;
import processing.core.*;

public class SpiroPApplet extends PApplet {
	private SpirotechnicMain<?> manager;

	private final MetadataDrawer metadataDrawer = new MetadataDrawer(this);

	private ProcessingToaster toaster;

	public static void main(String[] args) {

		Lg.setLogger(new JavaStdOutLogger());

		final String[] pargs = { "SpiroPapplet" };
		final SpiroPApplet sApp = new SpiroPApplet();

		PApplet.runSketch(pargs, sApp);
	}

	@Override
	public void settings() {
		size(InitialValues.INITIAL_WINDOW_WIDTH, InitialValues.INITIAL_WINDOW_HEIGHT, FX2D);
		smooth(8);
	}

	@Override
	public void setup() {

		frameRate(InitialValues.TARGET_FRAME_RATE);

		Toast.setToaster(toaster = new ProcessingToaster());

		ProcessingUtils.initialize(this);

		manager = new ProcessingSpirotechnicManager(this);
	}

	@Override
	public void draw() {
		this.background(50);
		manager.doFrame();
		metadataDrawer.drawMetadata();
		toaster.display();
	}

	@Override
	public void keyPressed() {
		// manager.getUserInterface().keyPressed(key, keyCode);
	}

	private final class ProcessingToaster implements Toast.Toaster {

		private String message = "";
		private int framesSinceKeyPress;

		@Override
		public void show(String message) {
			framesSinceKeyPress = 0;
			this.message = message;
		}

		public void display() {
			framesSinceKeyPress++;

			textAlign(PConstants.CENTER, PConstants.BOTTOM);
			textSize(24);

			// TODO: Make these calculations more structured
			final int alpha = 255 - 3 * framesSinceKeyPress;
			final int x = width / 2;
			final int y = height - 30;

			// draw Shadow
			fill(0, alpha);
			text(message, x + 2.0f, y + 2.0f);

			// drawText
			fill(255, alpha);
			text(message, x, y);
		}
	}

	//Save the screen at closing.
	@Override
	public void dispose() {
		save("ImageEnd.png");

		manager.close();
	}
}
