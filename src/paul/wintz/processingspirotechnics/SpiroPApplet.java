package paul.wintz.processingspirotechnics;

import javafx.scene.canvas.Canvas;
import paul.wintz.parametricequationdrawer.MainPresenter;
import paul.wintz.parametricequationdrawer.controllers.javafx.SpiroOptionsJavaFX;
import paul.wintz.processing.ProcessingToaster;
import paul.wintz.processing.ProcessingUtils;
import paul.wintz.utils.Toast;
import paul.wintz.utils.logging.JavaStdOutLogger;
import paul.wintz.utils.logging.Lg;
import processing.core.PApplet;

import static paul.wintz.parametricequationdrawer.InitialValues.*;

public class SpiroPApplet extends PApplet {
    private static final String TAG = Lg.makeTAG(SpiroPApplet.class);

    private MainPresenter<?> manager;
    private ProcessingToaster toaster;

    private static SpiroPApplet pApplet = new SpiroPApplet();
    private static SpiroOptionsJavaFX spiroOptionsJavaFX;

    public static void main(String[] args) {
        Lg.setLogger(new JavaStdOutLogger());
        PApplet.runSketch(new String[]{ "Spirotechnics" }, pApplet);
    }

    @Override
    public void settings() {
        size(INITIAL_WINDOW_WIDTH, INITIAL_WINDOW_HEIGHT, FX2D);
        smooth(8);
    }

    @Override
    public void setup() {
        try {
            frameRate(TARGET_FRAME_RATE);

            toaster = new ProcessingToaster(this, EnglishStrings.idMap());
            Toast.setToaster(toaster);

            ProcessingUtils.initialize(this);
            spiroOptionsJavaFX = new SpiroOptionsJavaFX();
            spiroOptionsJavaFX.setOnCloseRequest(event -> cleanup());

            // Setup the PApplet canvas to call cleanup() when it closes. This is likely to break due to implementation
            // changes by Processing.
            Canvas windowCanvas = (Canvas) pApplet.getSurface().getNative();
            windowCanvas.getScene().getWindow().setOnCloseRequest(event -> cleanup());

            manager = new MainPresenter<>(new ProcessingGraphicsIO(this), spiroOptionsJavaFX.getSpiroUserInterface());
        } catch (Exception e) {
            Lg.e(TAG, "Application setup failed", e);
            exitActual();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void draw() {
        this.background(50);
        manager.doFrame();
        toaster.display();
    }

    private static void cleanup(){
        Lg.i(TAG, "cleanup() - close pApplet canvas and javaFX controls");
        if(pApplet != null ){
            pApplet.save("ImageEnd.png");
            pApplet.manager.close();
            pApplet.exitActual();
        } else {
            Lg.w(TAG, "pApplet was null before cleanup!");
        }
        if(spiroOptionsJavaFX != null){
            spiroOptionsJavaFX.close();
        }
    }

    @Override
    public void keyPressed() {
        Toast.show("Key pressed: " + key);
    }

}
