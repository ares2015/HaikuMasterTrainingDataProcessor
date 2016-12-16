package preprocessing.reader;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Oliver on 12/16/2016.
 */
public class TextReaderTest {

    @Test
    public void testRead(){
        TextReader textReader = new TextReaderImpl("C:\\Users\\Oliver\\Documents\\NlpTrainingData\\TextData.txt");
        List<String> sentences = textReader.read();
        assertTrue(sentences.size() > 0);
    }

}
