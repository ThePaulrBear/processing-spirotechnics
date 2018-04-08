package paul.wintz.processingspirotechnics;

import static paul.wintz.parametricequationdrawer.InitialValues.*;

import paul.wintz.parametricequationdrawer.SpirotechnicMain;
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

        PApplet.runSketch(new String[]{ "Spirotechnics" }, new SpiroPApplet());
    }

    @Override
    public void settings() {
        size(INITIAL_WINDOW_WIDTH, INITIAL_WINDOW_HEIGHT, FX2D);
        smooth(8);
    }

    @Override
    public void setup() {

        frameRate(TARGET_FRAME_RATE);

        toaster = new ProcessingToaster();
        Toast.setToaster(toaster);

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
