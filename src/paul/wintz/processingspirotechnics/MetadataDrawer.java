package paul.wintz.processingspirotechnics;

import java.util.*;

import processing.core.PApplet;

public class MetadataDrawer {

    private static final int x = 5;
    private static final int textSize = 12;

    private final PApplet pApplet;

    public MetadataDrawer(PApplet pApplet) {
        this.pApplet = pApplet;
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
        // float y = textOriginY + (float) (- 1) * textSpacing + 5;
        // float x = textOriginX + j * segmentLength;
        // for (int f = 0; f < manager.getDrawingManager().getCyclesToDraw();
        // f++)
        // line(x, y, x + segmentLength, y);
        // }
        //      infoDrawer.draw();

    }

    public void drawMetadata() {
        pApplet.fill(255); // Make text white again!
        final List<String> lowerText = new ArrayList<>();//manager.getInfo();

        for (int i = 1; i <= lowerText.size(); i++) {
            pApplet.text(lowerText.get(lowerText.size() - i), x, lowerY(i));
        }
    }

    private int lowerY(int line) {
        return pApplet.height - (line + 1) * (textSize + 2);
    }

    private int upperY(int line) {
        return (1 + line) * (textSize + 2);
    }

    private class SpirotechnicInfoDrawer {

        public void draw() {
            // text settings

            //          textSize(textSize);
            //          textAlign(PConstants.LEFT, PConstants.TOP);
            //
            //          final List<ConditionStringPair> conditionStrings = manager.getConditionedStrings();
            //
            //          int lineNumber = 0;
            //          for (final ConditionStringPair pair : conditionStrings) {
            //
            //              switch (pair.getCondition()) {
            //              case DRAWING:
            //                  fill(255);
            //                  break;
            //              case INVISIBLE:
            //                  fill(0);
            //                  break;
            //              case VISIBLE:
            //                  fill(180);
            //                  break;
            //              }
            //
            //              for (final String s : pair.getText().split("\n")) {
            //                  text(s, x, upperY(lineNumber));
            //
            //                  lineNumber++;
            //              }
            //              lineNumber++;
            //          }
            //

        }


    }

    public static class ColoredString {
        public ColoredString(String text, int color) {
            this.text = text;
            this.color = color;
        }

        public String text;
        public int color;
    }

}
