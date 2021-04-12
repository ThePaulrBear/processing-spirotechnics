package paul.wintz.processingspirotechnics;

import paul.wintz.canvas.Layer;
import paul.wintz.parametricequationdrawer.GraphicsIO;
import paul.wintz.processing.PGraphicsLayer;
import paul.wintz.processing.ProcessingUtils;
import paul.wintz.utils.logging.Lg;
import processing.core.PApplet;
import processing.core.PGraphics;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.lang.Float.min;
import static paul.wintz.utils.logging.Lg.makeTAG;

class ProcessingGraphicsIO implements GraphicsIO<PGraphics> {
    private static final String TAG = makeTAG(ProcessingGraphicsIO.class);

    private final PGraphicsLayerFactory pGraphicsLayerFactory = new PGraphicsLayerFactory();
    private final PGraphicsToPAppletDisplayer pGraphicsToPAppletDisplayer;
    private final PGraphicsLayerCompositor pGraphicsLayerCompositor = new PGraphicsLayerCompositor();
    private final PGraphicsImageSaver pGraphicsImageSaver = new PGraphicsImageSaver();
    private final AnimationIO<PGraphics> processingGifRecording = new ProcessingGifRecording();
    private final AnimationIO<PGraphics> processingVideoRecording = new ProcessingVideoRecording();

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
    public AnimationIOFactory<PGraphics> getAnimationRecorderFactory() {
        return fileType -> {
            if("gif".equals(fileType)){
                return processingGifRecording;
            } else if ("mp4".equals(fileType)) {
                return processingVideoRecording;
            } else {
                throw new RuntimeException("File type not supported: " + fileType);
            }

        };
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
            final float maxWidthScale =  min(1, (float) pApplet.width / (float) image.width);
            final float maxHeightScale =  min(1, (float) pApplet.height / (float) image.height);
            final float displayScale = min(maxWidthScale, maxHeightScale);

            pApplet.scale(displayScale, displayScale);
            pApplet.image(image, 0, 0);
            pApplet.scale(1 / displayScale, 1 / displayScale);
        }
    }

    private static class PGraphicsImageSaver implements ImageSaver<PGraphics> {

        @Nonnull
        @Override
        public File save(PGraphics image, File directory, String baseName) throws IOException {
            File file = new File(directory, baseName + ".png");
            Lg.d(TAG, "File saving to: " + file);
            file.deleteOnExit();
            image.save(file.getAbsolutePath());
            return file;
        }
    }


}
