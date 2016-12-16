package preprocessing;

import org.junit.Test;
import tokens.Tokenizer;
import tokens.TokenizerImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oliver on 12/16/2016.
 */
public class SentencesPreprocessorTest {

    @Test
    public void testPreprocessing(){
        Tokenizer tokenizer = new TokenizerImpl();
        SentencesPreprocessor sentencesPreprocessor = new SentencePreprocessorImpl(tokenizer);
        List<String> sentences = new ArrayList<String>();
        sentences.add("this is [my] test");
        List<String> preprocessedSentences = sentencesPreprocessor.preprocess(sentences);
        assertEquals("this is test", preprocessedSentences.get(0));
    }
}
