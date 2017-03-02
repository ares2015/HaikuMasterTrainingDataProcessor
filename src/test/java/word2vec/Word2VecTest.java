package word2vec;

import com.HaikuMasterTrainingDataProcessor.preprocessor.SentencesPreprocessor;
import com.HaikuMasterTrainingDataProcessor.preprocessor.SentencesPreprocessorImpl;
import com.HaikuMasterTrainingDataProcessor.tagging.PosTagger;
import com.HaikuMasterTrainingDataProcessor.tagging.PosTaggerImpl;
import com.HaikuMasterTrainingDataProcessor.tokenizing.Tokenizer;
import com.HaikuMasterTrainingDataProcessor.tokenizing.TokenizerImpl;
import com.HaikuMasterTrainingDataProcessor.word2vec.analysis.Word2VecAnalyser;
import com.HaikuMasterTrainingDataProcessor.word2vec.analysis.Word2VecAnalyserImpl;
import com.HaikuMasterTrainingDataProcessor.word2vec.model.Word2VecModel;
import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcher;
import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcherImpl;
import org.apache.thrift.TException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Oliver on 12/17/2016.
 */
public class Word2VecTest {

    String inputFilePath = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTextData.txt";

    Tokenizer tokenizer = new TokenizerImpl();

    PosTagger posTagger = new PosTaggerImpl();

    SentencesPreprocessor sentencesPreprocessor = new SentencesPreprocessorImpl(posTagger);

    Word2VecAnalyser word2VecAnalyser = new Word2VecAnalyserImpl(sentencesPreprocessor);


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
