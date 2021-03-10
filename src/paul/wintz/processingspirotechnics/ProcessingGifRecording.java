package paul.wintz.processingspirotechnics;

import paul.wintz.parametricequationdrawer.GraphicsIO;
import paul.wintz.utils.Toast;
import paul.wintz.utils.logging.Lg;
import processing.core.PGraphics;
import sojamo.animatedgif.GifRecorder;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.Math.floor;
import static java.lang.Math.round;
import static paul.wintz.utils.Utils.checkPositive;
import static paul.wintz.utils.logging.Lg.makeTAG;

class ProcessingGifRecording implements GraphicsIO.AnimationIO<PGraphics> {
    private static final String TAG = makeTAG(ProcessingGifRecording.class);
    public static final long MB_LIMIT = 10L;

    private GifRecorder gifRecorder;

    private File file;
    private float fps;
    private int framesRecorded = 0;
    private boolean isOpen = false;
    private final static long BYTES_IN_MEGABYTE = 1024*1024;

    @Override
    public void open(File file) {
        checkState(!isOpen, "GifRecording is already open");
        checkState(gifRecorder == null, "gifRecorder must be null");

        this.file = checkNotNull(file);
        Lg.i(TAG, "Beginning GIF record to: " + file.getAbsolutePath());
        framesRecorded = 0;

        gifRecorder = new GifRecorder(500, 500);
        gifRecorder.clear();
        gifRecorder.setFile(file)
                .setFramesPerSecond((int) fps)
                .setLoop(true);

        isOpen = true;
    }

    @Override
    public String getExtension() {
        return ".gif";
    }

    @Override
    public void addFrame(PGraphics frame) {
        checkState(isOpen, "The GIF is not open");

        gifRecorder.addFrame(frame);

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
    public void close(@Nonnull Consumer<File> onFileFinished) {
        checkState(isOpen, "The GIF is not open");
        checkNotNull(gifRecorder);
        checkNotNull(file);

        isOpen = false;

        Lg.v(TAG, "Starting save");
        Executors.newSingleThreadExecutor().submit(new SaveGifFileRunnable(gifRecorder, file, onFileFinished));

        Lg.i(TAG, "Closing GIF. Number of frames: " + framesRecorded);

        gifRecorder = null;
        file = null;
    }

    @Override
    public void setFPS(float fps) {
        checkPositive(fps);
        Lg.i(TAG, "fps set to " + fps);
        this.fps = fps;
    }

    private class SaveGifFileRunnable implements Runnable {
        private final GifRecorder gifRecorder;
        private final File file;
        private final Consumer<File> onFileFinished;
        private final int framesRecorded = ProcessingGifRecording.this.framesRecorded;

        public SaveGifFileRunnable(GifRecorder gifRecorder, File file, Consumer<File> onFileFinished) {
            this.gifRecorder = checkNotNull(gifRecorder);
            this.file = checkNotNull(file);;
            this.onFileFinished = checkNotNull(onFileFinished);
        }

        @Override
        public void run() {
            try {
                Lg.v(TAG, "Thread started for saving GIF");
                gifRecorder.save(); // THIS IS TIME-CONSUMING.
                gifRecorder.clear();
                final double maxFrames = getMaxFramesToFitFileSize(MB_LIMIT * BYTES_IN_MEGABYTE, file, (double) framesRecorded);
                String message = String.format("Saving GIF finished. A maximum of %d frames will fit in %d bytes.", round(floor(maxFrames)), MB_LIMIT * BYTES_IN_MEGABYTE);
                Lg.i(TAG, message);
                Toast.show(message);
                onFileFinished.accept(file);
                Lg.v(TAG, "Thread finished for saving GIF.");
            }catch (Exception e) {
                Lg.e(TAG, "GIF Failed to save? ", e);
            }
        }
    }

    private static double getMaxFramesToFitFileSize(long maxSize, File file, double framesRecorded) {
        final double sizePerFrame = (double) file.length() / framesRecorded;
        return maxSize / sizePerFrame;
    }
}
