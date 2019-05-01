package paul.wintz.processingspirotechnics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnglishStringsTest {

    @Test
    public void verifyAllTranslationsAreIncluded() {
        // The static initializer will fail if any translations are missing.
        //noinspection ResultOfMethodCallIgnored
        EnglishStrings.idMap();
    }
}