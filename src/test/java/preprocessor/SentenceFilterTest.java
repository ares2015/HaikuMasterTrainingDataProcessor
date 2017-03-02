package preprocessor;

import com.HaikuMasterTrainingDataProcessor.preprocessor.filter.SentenceFilter;
import com.HaikuMasterTrainingDataProcessor.preprocessor.filter.SentenceFilterImpl;
import com.HaikuMasterTrainingDataProcessor.tokenizing.Tokenizer;
import com.HaikuMasterTrainingDataProcessor.tokenizing.TokenizerImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by oled on 3/2/2017.
 */
public class SentenceFilterTest {

    private Tokenizer tokenizer = new TokenizerImpl();

    private SentenceFilter sentenceFilter = new SentenceFilterImpl(tokenizer);

    @Test
    public void test() {
        assertEquals("dog is animal", sentenceFilter.filter("$ ' dog is animal"));
        assertEquals("dog's animal", sentenceFilter.filter("dog's animal"));
        assertEquals("dog’s animal", sentenceFilter.filter("dog’s animal ’"));
    }


}
