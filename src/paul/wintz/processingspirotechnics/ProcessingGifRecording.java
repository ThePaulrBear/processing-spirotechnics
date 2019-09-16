package paul.wintz.processingspirotechnics;

import paul.wintz.parametricequationdrawer.GraphicsIO;
import paul.wintz.utils.logging.Lg;
import processing.core.PGraphics;
import sojamo.animatedgif.GifRecorder;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static paul.wintz.utils.Utils.checkPositive;
import static paul.wintz.utils.logging.Lg.makeTAG;

class ProcessingGifRecording implements GraphicsIO.AnimationIO<PGraphics> {
    private static final String TAG = makeTAG(ProcessingGifRecording.class);

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
    public void close(Consumer<File> onFileFinished) {
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
                Lg.v(TAG, "Thread started");
                gifRecorder.save();
                gifRecorder.clear();
                printMaxFramesToBeUnderGivenFileSize(3L * BYTES_IN_MEGABYTE, file, framesRecorded);
                onFileFinished.accept(file);
                Lg.v(TAG, "Thread finished");
            }catch (Exception e) {
                Lg.e(TAG, "Failed to save? ", e);
            }
        }
    }

    private static void printMaxFramesToBeUnderGivenFileSize(long maxSize, File file, double framesRecorded) {
        final double sizePerFrame = (double) file.length() / framesRecorded;
        final double maxNumber = maxSize / sizePerFrame;
        Lg.i(TAG, "The maximum number of frames is: " + maxNumber + " to fit in " + maxSize + " bytes.");
    }
}
