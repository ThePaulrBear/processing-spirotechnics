package paul.wintz.processingspirotechnics;

import paul.wintz.canvas.Layer;
import paul.wintz.parametricequationdrawer.GraphicsIO;
import paul.wintz.processing.PGraphicsLayer;
import paul.wintz.processing.ProcessingUtils;
import paul.wintz.utils.logging.Lg;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static paul.wintz.parametricequationdrawer.InitialValues.SIDEBAR_WIDTH;
import static paul.wintz.utils.Utils.checkPositive;
import static paul.wintz.utils.logging.Lg.makeTAG;

class ProcessingGraphicsIO implements GraphicsIO<PGraphics> {
    private static final String TAG = makeTAG(ProcessingGraphicsIO.class);

    private final PGraphicsLayerFactory pGraphicsLayerFactory = new PGraphicsLayerFactory();
    private final PGraphicsToPAppletDisplayer pGraphicsToPAppletDisplayer;
    private final PGraphicsLayerCompositor pGraphicsLayerCompositor = new PGraphicsLayerCompositor();
    private final PGraphicsImageSaver pGraphicsImageSaver = new PGraphicsImageSaver();
    private final ProcessingGifRecording processingGifRecording = new ProcessingGifRecording();

    ProcessingGraphicsIO(PApplet pApplet) {
        pGraphicsToPAppletDisplayer = new PGraphicsToPAppletDisplayer(pApplet);
    }

    @Override
    public LayerFactory<PGraphics> getLayerFactory() {
        return pGraphicsLayerFactory;
    }

    @Override
    public ImageDisplayer<PGraphics> getImageDisplayer() {
        return pGraphicsToPAppletDisplayer;
    }

    @Override
    public ImageCompositor<PGraphics> getImageCompositor() {
        return pGraphicsLayerCompositor;
    }

    @Override
    public ImageSaver<PGraphics> getImageSaver() {
        return pGraphicsImageSaver;
    }

    @Override
    public AnimationIO<PGraphics> getAnimationRecorder() {
        return processingGifRecording;
    }

    private static final class PGraphicsLayerFactory implements LayerFactory<PGraphics> {

        @Override
        public Layer<PGraphics> makeLayer(int size) {
            return new PGraphicsLayer(size, size);
        }
    }

    private static final class PGraphicsLayerCompositor implements ImageCompositor<PGraphics> {

        final PGraphics base = ProcessingUtils.createPGraphics(1, 1);

        @Override
        public PGraphics compositeLayers(List<Layer<PGraphics>> layers) {

            base.setSize(layers.get(0).getWidth(), layers.get(0).getHeight());
            base.beginDraw();
            for (final Layer<PGraphics> layer : layers) {
                layer.drawOnto(base);
            }
            base.endDraw();
            return base;
        }
    }

    private static final class PGraphicsToPAppletDisplayer implements ImageDisplayer<PGraphics> {
        private final PApplet pApplet;

        PGraphicsToPAppletDisplayer(PApplet pApplet) {
            this.pApplet = pApplet;
        }

        @Override
        public void onDisplay(PGraphics image) {
            final int shortestEdge = Math.min(pApplet.height, pApplet.width - SIDEBAR_WIDTH);
            final float displayScale = (float) shortestEdge / (float) image.width;

            pApplet.scale(displayScale, displayScale);
            pApplet.image(image, SIDEBAR_WIDTH / displayScale, 0);
            pApplet.scale(1 / displayScale, 1 / displayScale);
        }
    }

    /**
     * Using this library: https://github.com/01010101/GifAnimation
     */
    private static class ProcessingGifRecording implements AnimationIO<PGraphics> {
        private static final String TAG = makeTAG(ProcessingGifRecording.class);

        private gifAnimation.GifMaker gifMaker;
        private File file;
        private float fps;
        private int framesRecorded = 0;
        private boolean isOpen = false;

        private void printMaxFramesToBeUnderGivenFileSize(long maxSize) {
            final double sizePerFrame = (double) file.length() / (double) framesRecorded;
            final double maxNumber = maxSize / sizePerFrame;
            Lg.i(TAG, "The maximum number of frames is: " + maxNumber);
        }

        @Override
        public void open(File file) {
            this.file = file;
            Lg.i(TAG, "Beginning GIF record to: " + file.getAbsolutePath());
            framesRecorded = 0;
            gifMaker = ProcessingUtils.newGifMaker(file);
            gifMaker.setRepeat(0);

            isOpen = true;
        }

        @Override
        public void addFrame(PGraphics frame) {
            checkState(isOpen, "The GIF is not open");

            final int frameDelayInMillis = (int) (1000.0f / fps);
            Lg.d(TAG, "delay: " + frameDelayInMillis);
            gifMaker.setDelay(frameDelayInMillis);
            gifMaker.addFrame(frame);
            framesRecorded++;
        }

        @Override
        public int getRecordedFrameCount() {
            if (isOpen)
                return framesRecorded;
            else
                return -1;
        }

        @Override
        public void close() {
            checkState(isOpen, "The GIF is not open");

            isOpen = false;

            gifMaker.finish();
            Lg.i(TAG, "GIF closed. Number of frames: " + framesRecorded);

            printMaxFramesToBeUnderGivenFileSize((long) 3e6);

            gifMaker = null;
            file = null;
        }

        @Override
        public void setFPS(float fps) {
            checkPositive(fps);
            Lg.i(TAG, "fps set to " + fps);
            this.fps = fps;
        }
    }

    private static class PGraphicsImageSaver implements ImageSaver<PGraphics> {

        @Override
        public File save(PGraphics image) throws IOException {
            File tempFile = File.createTempFile("spirotechnic", ".png");
            Lg.d(TAG, "File saving to: " + tempFile);
            tempFile.deleteOnExit();
            image.save(tempFile.getAbsolutePath());
            return tempFile;
        }

    }

}
