package paul.wintz.processingspirotechnics;

import paul.wintz.parametricequationdrawer.*;
import paul.wintz.processing.ProcessingUtils;
import paul.wintz.utils.Toast;
import paul.wintz.utils.logging.*;
import processing.core.*;

public class SpiroPApplet extends PApplet {
	private SpirotechnicMain<?> manager;

	private final SpirotechnicInfoDrawer infoDrawer = new SpirotechnicInfoDrawer();

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

		// clear the PApplet window
		this.background(50);

		manager.doFrame();

		drawText();

		toaster.display();
	}

	@Override
	public void keyPressed() {
		// manager.getUserInterface().keyPressed(key, keyCode);
	}

	void drawText() {
		// final int divisions = 25;
		// SampleLine sampleLine = colorManager.getSampleLine(divisions);
		// // DRAW COLOR BARS BENEATH TEXT
		// for (int j = 0; j < divisions; j++) {
		// strokeWeight(sampleLine.getThicknesses(j));
		// stroke(sampleLine.getColors(j));
		//
		// float barLength= 100;
		// float segmentLength = barLength / divisions;
		// float y = textOrginY + (float) (- 1) * textSpacing + 5;
		// float x = textOrginX + j * segmentLength;
		// for (int f = 0; f < manager.getDrawingManager().getCyclesToDraw();
		// f++)
		// line(x, y, x + segmentLength, y);
		// }
		infoDrawer.draw();

	}

	private class SpirotechnicInfoDrawer {
		final int x = 5;
		final int textSize = 12;

		public void draw() {
			// text settings

			//			textSize(textSize);
			//			textAlign(PConstants.LEFT, PConstants.TOP);
			//
			//			final List<ConditionStringPair> conditionStrings = manager.getConditionedStrings();
			//
			//			int lineNumber = 0;
			//			for (final ConditionStringPair pair : conditionStrings) {
			//
			//				switch (pair.getCondition()) {
			//				case DRAWING:
			//					fill(255);
			//					break;
			//				case INVSIBLE:
			//					fill(0);
			//					break;
			//				case VISIBLE:
			//					fill(180);
			//					break;
			//				}
			//
			//				for (final String s : pair.getText().split("\n")) {
			//					text(s, x, upperY(lineNumber));
			//
			//					lineNumber++;
			//				}
			//				lineNumber++;
			//			}
			//
			//			fill(255); // Make text white again!
			//			final List<String> lowerText = new ArrayList<>();//manager.getInfo();
			//			lowerText.add(0, "frameRate: " + round(frameRate) + " fps");
			//
			//			for (int i = 1; i <= lowerText.size(); i++) {
			//				text(lowerText.get(lowerText.size() - i), x, lowerY(i));
			//			}
		}

		private int upperY(int line) {
			return (1 + line) * (textSize + 2);
		}

		private int lowerY(int line) {
			return height - (line + 1) * (textSize + 2);
		}

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

	/**
	 * Save the screen at closing.
	 */
	@Override
	public void dispose() {
		save("ImageEnd.png");

		manager.close();
	}
}
