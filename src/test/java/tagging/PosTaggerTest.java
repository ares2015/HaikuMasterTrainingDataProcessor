package tagging;

import com.HaikuMasterTrainingDataProcessor.tagging.PosTagger;
import com.HaikuMasterTrainingDataProcessor.tagging.PosTaggerImpl;
import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Oliver on 12/17/2016.
 */
public class PosTaggerTest {

    @Test
    public void testTag(){
        PosTagger posTagger = new PosTaggerImpl();
        String sentence = "Donald Trump defeated Hillary Clinton heavily in US presidential elections in November 2016";
        List<TokenTagData> tokenTagDataList = posTagger.tag(sentence);
        assertTrue(tokenTagDataList.size() > 0);
        assertTrue(tokenTagDataList.get(0).isNoun());
        assertTrue(tokenTagDataList.get(1).isNoun());
        assertTrue(tokenTagDataList.get(2).isVerb());
        assertTrue(tokenTagDataList.get(3).isNoun());
        assertTrue(tokenTagDataList.get(4).isNoun());
        assertTrue(tokenTagDataList.get(5).isAdverb());
        assertTrue(tokenTagDataList.get(6).isNoun());
        assertTrue(tokenTagDataList.get(7).isAdjective());
        assertTrue(tokenTagDataList.get(8).isNoun());
        assertTrue(tokenTagDataList.get(9).isNoun());
    }
}