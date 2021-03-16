package paul.wintz.processingspirotechnics;

import org.apache.commons.io.FileUtils;
import paul.wintz.parametricequationdrawer.GraphicsIO;
import paul.wintz.utils.logging.Lg;
import processing.core.PGraphics;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static paul.wintz.utils.Utils.checkPositive;
import static paul.wintz.utils.logging.Lg.makeTAG;

class ProcessingVideoRecording implements GraphicsIO.AnimationIO<PGraphics> {
    private static final String TAG = makeTAG(ProcessingVideoRecording.class);

    private File outputFile;
    private float fps;
    private int framesRecorded = 0;
    private boolean isOpen = false;

    private Path tempDirectory;

    @Override
    public void open(File outputFile) {
        checkState(!isOpen, "Recording is already open");

        this.outputFile = checkNotNull(outputFile);
        Lg.i(TAG, "Beginning animation record to: " + outputFile.getAbsolutePath());
        framesRecorded = 0;

        try {
            tempDirectory = Files.createTempDirectory("spyroVideo");

        } catch (IOException e) {
            Lg.e(TAG, "Failed to create temporary directory for video frames", e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            // We need to save the directory object so that it isn't garbage collected before the thread runs.
            File directory = tempDirectory.toFile();
            @Override
            public void run() {
                Lg.i(TAG, "Deleting temporary directory(s) of animation frames.");
                try {
                    FileUtils.deleteDirectory(directory);
                    Lg.v(TAG, "Deleted %s.", directory);
                } catch (IOException e) {
                    Lg.e(TAG, "Could not delete %s.", directory);
                }
            }
        });

        isOpen = true;
    }

    @Override
    public String getExtension() {
        return ".mp4";
    }

    @Override
    public void addFrame(PGraphics frame) {
        checkState(isOpen, "The recording is not open");

        String filename = String.format("%s/%05d.tif", tempDirectory.toString(), framesRecorded);
        Lg.v(TAG, "Saving video frame to %s", filename);
        frame.save(filename);

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
        checkState(isOpen, "The recording is not open");
        checkNotNull(outputFile);
        checkNotNull(tempDirectory);

        Lg.i(TAG, "Starting thread to finalize video. Number of frames: %d",framesRecorded);
        Executors.newSingleThreadExecutor().submit(new FinalizeVideoRunnable(tempDirectory.toFile(), outputFile, onFileFinished, fps));

        isOpen = false;
        tempDirectory = null;
        outputFile = null;
    }

    @Override
    public void setFPS(float fps) {
        checkPositive(fps);
        Lg.i(TAG, "fps set to " + fps);
        this.fps = fps;
    }

    private class FinalizeVideoRunnable implements Runnable {
        private final File tempDirectory;
        private final File outputFile;
        private final Consumer<File> onFileFinished;
        private String fps;
        private final int framesRecorded = ProcessingVideoRecording.this.framesRecorded;

        public FinalizeVideoRunnable(File tempDirectory, File outputFile, Consumer<File> onFileFinished, float fps) {
            this.tempDirectory = tempDirectory;
            this.outputFile = checkNotNull(outputFile);
            this.onFileFinished = checkNotNull(onFileFinished);
            this.fps = Float.toString(fps);
        }

        @Override
        public void run() {
            Lg.v(TAG, "Thread started to save video");
            String inputFileFormat = "%05d.tif";
            String outputFileString = "\"" + outputFile + "\""; // "\"C:\\Users\\PaulWintz\\Google Drive\\Projects\\Spirotechnics Media\\recordedGIFs\\output.mp4\"";
//            "ffmpeg -framerate 30 -pattern_type glob -i '*.png' -i example.mp3 -c:v libx264 \\ -r 30 -pix_fmt yuv420p out.mp4"

            int loops = 5;
            try {
                ProcessBuilder pb = new ProcessBuilder(
                        "ffmpeg",
                        "-loglevel", "error", // only print out errors.
                        "-i", inputFileFormat,
                        "-framerate", fps, // frames per second.
                        "-filter_complex", String.format("loop=loop=%d:size=%d:start=0", loops - 1, framesRecorded),
                        "-codec:v", "libx264", // The order is important here. The output codec should come before the output file.
                        "-crf", "20", // quality, with 1 the lowest and 50 the highest.
                        outputFileString);
                pb.directory(tempDirectory);
                pb.inheritIO(); // Print all the output to the program output.
                Process p = pb.start();
                Lg.v(TAG, "Started ffmpeg thread.");
                try {
                    p.waitFor();
                } catch (InterruptedException ignored) {}
                Lg.v(TAG, "Finished ffmpeg thread.");

            } catch (IOException e) {
                Lg.e(TAG, "Saving video failed", e);
                return;
            }
            Lg.v(TAG, "Finished saving video to %s", outputFileString);

            onFileFinished.accept(outputFile);
        }
    }

}
