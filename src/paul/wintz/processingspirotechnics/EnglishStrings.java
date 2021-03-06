package paul.wintz.processingspirotechnics;

import paul.wintz.stringids.StringIdMap;
import paul.wintz.stringids.StringIds;

import static paul.wintz.stringids.StringIds.*;

class EnglishStrings {
    private static final StringIdMap ID_MAP = StringIdMap.builder()
            .setRequiredIds(StringIds.values())
            .putStringId(IS_RECORDING_CHANGED, "Is recording? %b")
            .putStringId(FPS_CHANGED_TO, "FPS set to %.1f")
            .putStringId(IMAGE_SAVED_TO, "Image saved to\n%s")
            .putStringId(FAILED_IMAGE_SAVE, "Image failed to save to\n%s")
            .putStringId(SEGMENT_COUNT_CHANGED_TO, "Segment count changed to %d")
            .putStringId(CONTACT_CIRCLE_ROTATION_CHANGED, "Rotation Offset Circle Changed to %d")
            .putStringId(LOADED_GRAPH, "Loaded graph:\nRadii: %s\nVelocities: %s")
            .putStringId(CANVAS_SIZE_CHANGED, "Canvas size changed to %dx%d")
            .putStringId(ZOOM_CHANGED_TO, "Zoom changed to %.2fx")
            .putStringId(FRAME_SAVED, "Frame #%d of %d saved")
            .build();

    static StringIdMap idMap() {
        return ID_MAP;
    }
}
