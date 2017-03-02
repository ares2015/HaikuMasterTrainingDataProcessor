package morphology;

import com.HaikuMasterTrainingDataProcessor.morphology.MorphologicalProcessor;
import com.HaikuMasterTrainingDataProcessor.morphology.MorphologicalProcessorImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by oled on 3/2/2017.
 */
public class MorphologicalProcessorTest {

    private MorphologicalProcessor morphologicalProcessor = new MorphologicalProcessorImpl();

    @Test
    public void test() {
        assertEquals("jump", morphologicalProcessor.removeSuffix("jumping"));
        assertEquals("jump", morphologicalProcessor.removeSuffix("jumped"));

    }
}
