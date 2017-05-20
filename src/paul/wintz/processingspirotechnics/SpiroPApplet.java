package paul.wintz.processingspirotechnics;

import java.util.ArrayList;

import paul.wintz.spirotechnics.InitialValues;
import paul.wintz.utils.Utils;
import processing.core.PApplet;
import processing.core.PConstants;

@SuppressWarnings("serial")
public class SpiroPApplet extends PApplet {		
	private ProcessingSpirotechnicManager manager;
	
	public static void main(String... args){
		String[] pargs = {"SpiroPapplet"};
		SpiroPApplet sApp = new SpiroPApplet();
		PApplet.runSketch(pargs, sApp);
	}
	
	@Override
	public void settings(){
		size(InitialValues.INITIAL_WINDOW_WIDTH, InitialValues.INITIAL_WINDOW_HEIGHT, JAVA2D);
//		smooth(8);
		
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

		drawText(manager.getUserInterface().getEditNumb());
	}

	@Override
	public void keyPressed() {
		manager.getUserInterface().keyPressed(key, keyCode);
	}

	private static class StringTuple {
		public StringTuple(String line, int color) {
			this.line = line;
			this.color = color;
		}
		private String line;
		private int color;
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
		
		
		ArrayList<StringTuple> lines = new ArrayList<>();
		lines.add(new StringTuple("R[0]: " + manager.getSpirotechnic().getRadiusInt(0) , 255));
		
		int lastSpiroTextToShow = Math.max(manager.getSpirotechnic().getFinalSpiroDisplayed(), editNumb) + 1;
		for(int i = 1; i < lastSpiroTextToShow; i++){
			int color = manager.getColorManager().getCircleColor(i, manager.getSpirotechnic().getIsGraphSpiro(i), editNumb);
			
			lines.add(new StringTuple("\n", 0));
			lines.add(new StringTuple("R: " + manager.getSpirotechnic().getRadius(i-1), color));
			lines.add(new StringTuple("V: " + manager.getSpirotechnic().getVelocity(i-1) + " (+" + manager.getSpirotechnic().getRotationOffeset(i) + ")", color));
			lines.add(new StringTuple("\n", 0));
			lines.add(new StringTuple("    R[" + i + "]: " + manager.getSpirotechnic().getRadiusInt(i), color));
			lines.add(new StringTuple("    " + Utils.PSI + "[" + i + "]" + manager.getSpirotechnic().getdPsiInt(i), color));	
		}
		
		for(int i = 0; i < lines.size(); i++){
			StringTuple tup = lines.get(i);
			fill(tup.color);
			text(tup.line, x, (1 + i) * textSize); 
			
		}
		
		fill(255); //Make text white again!
		String[] lowerText = { //TODO: Clean up this section. Replace messy calls
				String.format("dTime: %e", manager.getDrawingManager().getDTime()), 
				"frameRate: " + round(frameRate) + " fps",
				"frame: " + manager.getAnimationManager().getFrameNumber() + "/" + manager.getAnimationManager().getFramesPerAnimationCycle(),
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
