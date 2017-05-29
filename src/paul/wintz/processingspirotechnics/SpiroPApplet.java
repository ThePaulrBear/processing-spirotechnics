package paul.wintz.processingspirotechnics;

import java.util.ArrayList;
import java.util.List;

import paul.wintz.spirotechnics.InitialValues;
import paul.wintz.spirotechnics.SpirotechnicManager;
import paul.wintz.spirotechnics.spirotechnic.SpirotechnicInformant.ConditionStringPair;
import processing.core.PApplet;
import processing.core.PConstants;

public class SpiroPApplet extends PApplet {
	private SpirotechnicManager<?> manager;

	private final SpirotechnicInfoDrawer infoDrawer = new SpirotechnicInfoDrawer();

	public static void main(String[] args) {
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
		// Initialize the ProcessingUtils class for static access to processing
		// methods
		new ProcessingUtils(this);

		manager = new ProcessingSpirotechnicManager(this);
	}

	@Override
	public void draw() {

		// clear the PApplet window
		this.background(50);

		manager.drawFrame();

		drawText();
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

			textSize(textSize);
			textAlign(PConstants.LEFT, PConstants.TOP);

			final List<ConditionStringPair> conditionStrings = manager.getInformant().getConditionedStrings();

			int lineNumber = 0;
			for (final ConditionStringPair pair : conditionStrings) {

				switch (pair.getCondition()) {
				case DRAWING:
					fill(255);
					break;
				case INVSIBLE:
					fill(0);
					break;
				case VISIBLE:
					fill(180);
					break;
				}

				for (final String s : pair.getText().split("\n")) {
					text(s, x, upperY(lineNumber));

					lineNumber++;
				}
				lineNumber++;
			}

			fill(255); // Make text white again!
			final ArrayList<String> lowerText = manager.getInfo();
			lowerText.add(0, "frameRate: " + round(frameRate) + " fps");

			for (int i = 1; i <= lowerText.size(); i++) {
				text(lowerText.get(lowerText.size() - i), x, lowerY(i));
			}
		}

		private int upperY(int line) {
			return (1 + line) * (textSize + 2);
		}

		private int lowerY(int line) {
			return height - (line + 1) * (textSize + 2);
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
