package word2vec;

import org.apache.thrift.TException;
import org.junit.Test;
import word2vec.analysis.Word2VecAnalyser;
import word2vec.analysis.Word2VecAnalyserImpl;
import word2vec.model.Word2VecModel;
import word2vec.search.Word2VecSearcher;
import word2vec.search.Word2VecSearcherImpl;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Oliver on 12/17/2016.
 */
public class Word2VecTest {

    String inputFilePath = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTextData.txt";
    String outputFilePath = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\Word2Vec.txt";
    Word2VecAnalyser word2VecAnalyser = new Word2VecAnalyserImpl(inputFilePath, outputFilePath);


    @Test
    public void testCBOW() throws IOException, TException, InterruptedException {
        Word2VecModel word2VecModel = word2VecAnalyser.analyseCBOW();
    }

    @Test
    public void testSkipgram() throws IOException, TException, InterruptedException {
        Word2VecModel word2VecModel = word2VecAnalyser.analyseSkipgram();
    }

    @Test
    public void testSearchCBOW() throws InterruptedException, TException, IOException, Word2VecSearcher.UnknownWordException {
        Word2VecModel word2VecModel = word2VecAnalyser.analyseCBOW();
        Word2VecSearcher word2VecSearcher = new Word2VecSearcherImpl(word2VecModel);
        List<Word2VecSearcher.Match> matches = word2VecSearcher.getMatches("love", 10);
        assertTrue(matches.size() > 0);
        for (Word2VecSearcher.Match match : matches) {
            System.out.println(match.match() + " " + match.distance());
        }
    }

    @Test
    public void testSearchSkipgram() throws InterruptedException, TException, IOException, Word2VecSearcher.UnknownWordException {
        Word2VecModel word2VecModel = word2VecAnalyser.analyseSkipgram();
        Word2VecSearcher word2VecSearcher = new Word2VecSearcherImpl(word2VecModel);
        List<Word2VecSearcher.Match> matches = word2VecSearcher.getMatches("love", 10);
        assertTrue(matches.size() > 0);
        for (Word2VecSearcher.Match match : matches) {
            System.out.println(match.match() + " " + match.distance());
        }
    }

}
