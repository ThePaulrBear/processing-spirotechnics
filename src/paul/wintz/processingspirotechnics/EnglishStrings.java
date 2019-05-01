package paul.wintz.processingspirotechnics;

import paul.wintz.stringids.StringIdMap;
import paul.wintz.stringids.StringIds;

import static paul.wintz.stringids.StringIds.FRAME_TRANISITIONER_CHANGED;
import static paul.wintz.stringids.StringIds.*;

class EnglishStrings {
    private static final StringIdMap ID_MAP = StringIdMap.builder()
            .setRequiredIds(StringIds.values())
            .putStringId(IS_RECORDING_CHANGED, "Is recording? %b")
            .putStringId(FPS_CHANGED_TO, "FPS set to %.1f")
            .putStringId(FRAME_TRANISITIONER_CHANGED, "Frame transitioner changed to %s")
            .putStringId(IMAGE_SAVED_TO, "Image saved to\n%s")
            .putStringId(FAILED_IMAGE_SAVE, "Image failed to save to\n%s")
            .putStringId(SEGMENT_COUNT_CHANGED_TO, "Segment count changed to %d")
            .putStringId(CONTACT_CIRCLE_ROTATION_CHANGED, "Rotation Offset Circle Changed to %d")
            .putStringId(LOADED_GRAPH, "Loaded graph:\nRadii: %s\nVelocities: %s")
            .build();

    static StringIdMap idMap() {
        return ID_MAP;
    }
}
