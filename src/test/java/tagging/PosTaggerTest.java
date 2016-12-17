package tagging;

import com.HaikuMasterTrainingDataProcessor.tagging.PosTagger;
import com.HaikuMasterTrainingDataProcessor.tagging.PosTaggerImpl;
import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Oliver on 12/17/2016.
 */
public class PosTaggerTest {

    @Test
    public void testTag(){
        PosTagger posTagger = new PosTaggerImpl();
        String sentence = "Donald Trump defeated Hillary Clinton in US presidential elections in November 2016";
        List<TokenTagData> tokenTagDataList = posTagger.tag(sentence);
        assertTrue(tokenTagDataList.size() > 0);
        assertEquals("NNP", tokenTagDataList.get(0).getTag());
        assertEquals("NNP", tokenTagDataList.get(1).getTag());
        assertEquals("VBD", tokenTagDataList.get(2).getTag());
        assertEquals("NNP", tokenTagDataList.get(3).getTag());
        assertEquals("NNP", tokenTagDataList.get(4).getTag());
        assertEquals("NNP", tokenTagDataList.get(5).getTag());
        assertEquals("JJ", tokenTagDataList.get(6).getTag());
        assertEquals("NNS", tokenTagDataList.get(7).getTag());
        assertEquals("NNP", tokenTagDataList.get(8).getTag());


    }
}
