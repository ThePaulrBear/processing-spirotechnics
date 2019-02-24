package paul.wintz.processingspirotechnics;

import paul.wintz.parametricequationdrawer.MainPresenter;
import paul.wintz.processing.ProcessingToaster;
import paul.wintz.processing.ProcessingUtils;
import paul.wintz.utils.Toast;
import paul.wintz.utils.logging.JavaStdOutLogger;
import paul.wintz.utils.logging.Lg;
import processing.core.PApplet;
import paul.wintz.parametricequationdrawer.controllers.javafx.SpiroOptionsJavaFX;

import static paul.wintz.parametricequationdrawer.InitialValues.*;

public class SpiroPApplet extends PApplet {
    private static final String TAG = Lg.makeTAG(SpiroPApplet.class);

    private MainPresenter<?> manager;
    private ProcessingToaster toaster;

    private static SpiroPApplet pApplet = new SpiroPApplet();

    public static void main(String[] args) {
        Lg.setLogger(new JavaStdOutLogger());
        PApplet.runSketch(new String[]{ "Spirotechnics" }, pApplet);
        pApplet.setCloseListener(SpiroPApplet::cleanup);
    }

    private static void cleanup(){
        if(pApplet != null ){
            pApplet.exitActual();
        }
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

            toaster = new ProcessingToaster(this);
            Toast.setToaster(toaster);

            ProcessingUtils.initialize(this);

            SpiroOptionsJavaFX spiroOptionsJavaFX = new SpiroOptionsJavaFX();
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

    @Override
    public void keyPressed() {
        Toast.show("Key pressed: " + key);
    }

    interface CloseListener {
        void onClose();
    }

    private CloseListener closeListener = () -> {};

    @SuppressWarnings("WeakerAccess")
    public void setCloseListener(CloseListener closeListener) {
        this.closeListener = closeListener;
    }

    //Save the screen at closing.
    @Override
    public void dispose() {
        save("ImageEnd.png");

        manager.close();
        closeListener.onClose();
    }

}
