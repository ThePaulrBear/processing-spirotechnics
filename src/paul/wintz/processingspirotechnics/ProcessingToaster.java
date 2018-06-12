package paul.wintz.processingspirotechnics;

import paul.wintz.utils.Toast;
import processing.core.PConstants;

final class ProcessingToaster implements Toast.Toaster {

    private SpiroPApplet spiroPApplet;
    private String message = "";
    private int framesSinceKeyPress;

    public ProcessingToaster(SpiroPApplet spiroPApplet) {
        this.spiroPApplet = spiroPApplet;
    }

    @Override
    public void show(String message) {
        framesSinceKeyPress = 0;
        this.message = message;
    }

    void display() {
        framesSinceKeyPress++;

        spiroPApplet.textAlign(PConstants.CENTER, PConstants.BOTTOM);
        spiroPApplet.textSize(24);

        // TODO: Make these calculations more structured
        final int alpha = 255 - 8 * framesSinceKeyPress;
        final int x = spiroPApplet.width / 2;
        final int y = spiroPApplet.height - 30;

        // draw Shadow
        spiroPApplet.fill(0, alpha);
        spiroPApplet.text(message, x + 2.0f, y + 2.0f);

        // drawText
        spiroPApplet.fill(255, alpha);
        spiroPApplet.text(message, x, y);
    }
}
