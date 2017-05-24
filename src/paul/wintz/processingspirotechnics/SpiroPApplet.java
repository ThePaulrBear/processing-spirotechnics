package paul.wintz.processingspirotechnics;

import java.util.ArrayList;

import paul.wintz.spirotechnics.InitialValues;
import paul.wintz.spirotechnics.SpirotechnicManager;
import paul.wintz.utils.Utils;
import processing.core.PApplet;
import processing.core.PConstants;

public class SpiroPApplet extends PApplet {		
	private SpirotechnicManager<?> manager;
	
	public static void main(String[] args){
		String[] pargs = {"SpiroPapplet"};
		SpiroPApplet sApp = new SpiroPApplet();
		PApplet.runSketch(pargs, sApp);
	}
	
	@Override
	public void settings(){
		size(InitialValues.INITIAL_WINDOW_WIDTH, InitialValues.INITIAL_WINDOW_HEIGHT, FX2D);
		smooth(8);
		
	}
	
	@Override
	public void setup() {

		frameRate(InitialValues.TARGET_FRAME_RATE);
		//Initialize the ProcessingUtils class for static access to processing methods
		new ProcessingUtils(this);
		
		manager = new ProcessingSpirotechnicManager(this);

	}

	@Override
	public void draw() {
		
		//clear the PApplet window
		this.background(50);
		
		manager.drawFrame();

		drawText(-1/*manager.getUserInterface().getEditNumb()*/);
	}

	@Override
	public void keyPressed() {
		manager.getUserInterface().keyPressed(key, keyCode);
	}

	void drawText(int editNumb) {
//		final int divisions = 25;
//		SampleLine sampleLine = colorManager.getSampleLine(divisions);
//		// DRAW COLOR BARS BENEATH TEXT
//		for (int j = 0; j < divisions; j++) {
//			strokeWeight(sampleLine.getThicknesses(j));  
//			stroke(sampleLine.getColors(j));
//
//			float barLength= 100;
//			float segmentLength = barLength / divisions;
//			float y = textOrginY + (float) (- 1) * textSpacing + 5;
//			float x = textOrginX + j * segmentLength;
//			for (int f = 0; f < manager.getDrawingManager().getCyclesToDraw(); f++)
//				line(x, y, x + segmentLength, y);
//		}

		// text settings
		final int x = 5;
		final int textSize = 12;
		textSize(textSize);
		textAlign(PConstants.LEFT, PConstants.TOP);
		
		
		ArrayList<Utils.ColoredString> lines = new ArrayList<>();
		lines.add(new Utils.ColoredString("R[0]: " + manager.getSpirotechnic().getRadiusInt(0) , 255));
		
		int lastSpiroTextToShow = Math.max(manager.getSpirotechnic().getFinalSpiroDisplayed(), editNumb) + 1;
		for(int i = 1; i < lastSpiroTextToShow; i++){
			int color = manager.getColorManager().getCircleColor(i, manager.getSpirotechnic().getIsGraphSpiro(i), editNumb);
			
			lines.add(new Utils.ColoredString("\n", 0));
			lines.add(new Utils.ColoredString("R: " + manager.getSpirotechnic().getRadius(i-1), color));
			lines.add(new Utils.ColoredString("V: " + manager.getSpirotechnic().getVelocity(i-1) + " (+" + manager.getSpirotechnic().getRotationOffeset(i) + ")", color));
			lines.add(new Utils.ColoredString("\n", 0));
			lines.add(new Utils.ColoredString("    R[" + i + "]: " + manager.getSpirotechnic().getRadiusInt(i), color));
			lines.add(new Utils.ColoredString("    " + Utils.PSI + "[" + i + "]" + manager.getSpirotechnic().getdPsiInt(i), color));	
		}
		
		for(int i = 0; i < lines.size(); i++){
			Utils.ColoredString tup = lines.get(i);
			fill(tup.color);
			text(tup.text, x, (1 + i) * textSize); 
			
		}
		
		fill(255); //Make text white again!
		String[] lowerText = { //TODO: Clean up this section. Replace messy calls
				String.format("dTime: %e", manager.getDrawingManager().getDTime()), 
				"frameRate: " + round(frameRate) + " fps",
				"frame: " + manager.getAnimationManager().getFrameNumber() + "/" + manager.getAnimationManager().getAnimationLength(),
				(manager.getAnimationManager().isRecording())? "Currently Recording" : "Not Recording",
				String.format("t/Period: %.3f/%.3f", manager.getDrawingManager().getTime(), manager.getSpirotechnic().getPeriod()),
				String.format("Frames per Period: %.3f", manager.getSpirotechnic().getPeriod() / manager.getDrawingManager().getDTime()),
					
		};

		for (int i = 1; i <= lowerText.length; i++) {
			text(lowerText[lowerText.length - i], x, height - (i + 1) * textSize);
		}
	}

	/**
	 * Save the screen at closing. 
	 */
	@Override
	public void dispose() {
		this.save("ImageEnd.png");
		
		manager.close();
	}
}
