package paul.wintz.processingspirotechnics;

import static paul.wintz.spirotechnics.InitialValues.SIDEBAR_WIDTH;

import java.awt.EventQueue;

import paul.wintz.spirotechnics.SpirotechnicManager;
import paul.wintz.spirotechnics.userinterface.UI;
import paul.wintz.spirotechnics.userinterface.swinggui.OptionsJFrame;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class ProcessingSpirotechnicManager extends SpirotechnicManager<PGraphics> {
	private final PApplet pApplet;
	
	public ProcessingSpirotechnicManager(PApplet pApplet) {
		super(new PGraphicsCanvas(400, 400, 2), new MyDisplayer(pApplet), new MyToaster(pApplet), new MyLogger(pApplet));
		this.pApplet = pApplet;
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OptionsJFrame frame = new OptionsJFrame(ProcessingSpirotechnicManager.this.getUserInterface().allOptions);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	int framesSinceKeyPress = Integer.MAX_VALUE / 2;
	
	/**
	 * Draw text to the PApplet that describes the most recent UI action.
	 * The text fades away over a set number of frames.  
	 * @param pApplet
	 */
	private final static class MyToaster extends SpirotechnicManager.ToastCallback {
		private final PApplet pApplet;
		public MyToaster(PApplet pApplet) {
			this.pApplet = pApplet;
		}

		protected void onDrawToast(){
			framesSinceKeyPress++;

			pApplet.textAlign(PConstants.CENTER, PConstants.BOTTOM);
			pApplet.textSize(24);
			int alpha = 255 - 3 * framesSinceKeyPress; //TODO: Make these calculations more structured
			int x = pApplet.width / 2;
			int y = pApplet.height - 30;

			// draw Shadow
			pApplet.fill(0, alpha);
			pApplet.text(message, x + 2, y + 2);

			// drawText
			pApplet.fill(255, alpha);
			pApplet.text(message, x, y);	
		}
	}
	
	final static class MyLogger implements SpirotechnicManager.LogCallback {
		private final PApplet pApplet;
		public MyLogger(PApplet pApplet) {
			this.pApplet = pApplet;
		}

		@Override
		public void logStd(String message) {
			System.out.println(message);
		}

		@Override
		public void logError(String message) {
			System.err.println(message);
		}

		@Override
		public void logError(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	final static class MyDisplayer implements SpirotechnicManager.DisplayCallback<PGraphics> {
		private final PApplet pApplet;
		public MyDisplayer(PApplet pApplet) {
			this.pApplet = pApplet;
		}

		@Override
		public void onDisplay(PGraphics image) {
			float displayScale = (float) Math.min(pApplet.height, pApplet.width - SIDEBAR_WIDTH ) / (float) image.width;
			pApplet.scale(displayScale, displayScale);
			pApplet.image(image, SIDEBAR_WIDTH / displayScale, 0);
			pApplet.scale(1 / displayScale, 1 / displayScale);
		}
	}

}
