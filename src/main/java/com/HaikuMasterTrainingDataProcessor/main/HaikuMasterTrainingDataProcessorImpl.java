package com.HaikuMasterTrainingDataProcessor.main;

import com.HaikuMasterTrainingDataProcessor.tokens.Tokenizer;
import com.HaikuMasterTrainingDataProcessor.tokens.TokenizerImpl;
import com.HaikuMasterTrainingDataProcessor.word2vec.analysis.Word2VecAnalyser;
import com.HaikuMasterTrainingDataProcessor.word2vec.analysis.Word2VecAnalyserImpl;
import com.HaikuMasterTrainingDataProcessor.word2vec.model.Word2VecModel;
import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcher;
import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcherImpl;
import org.apache.thrift.TException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Oliver on 12/17/2016.
 */
public class HaikuMasterTrainingDataProcessorImpl implements HaikuMasterTrainingDataProcessor {

    String inputFilePath = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTextData.txt";
    String outputFilePath = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\Word2Vec.txt";
    Word2VecAnalyser word2VecAnalyser = new Word2VecAnalyserImpl(inputFilePath, outputFilePath);
    private Tokenizer tokenizer = new TokenizerImpl();

    public static void main(String[] args) throws InterruptedException, TException, Word2VecSearcher.UnknownWordException, IOException {
        HaikuMasterTrainingDataProcessor haikuMasterTrainingDataProcessor = new HaikuMasterTrainingDataProcessorImpl();
        haikuMasterTrainingDataProcessor.process();
    }

    @Override
    public void process() throws InterruptedException, TException, IOException, Word2VecSearcher.UnknownWordException {
        Word2VecModel word2VecModel = word2VecAnalyser.analyseCBOW();
        Iterable<String> vocab = word2VecModel.getVocab();
        for (String sentence : vocab) {
            List<String> tokens = tokenizer.getTokens(sentence);
            Word2VecSearcher word2VecSearcher = new Word2VecSearcherImpl(word2VecModel);
            for (String token : tokens) {
                List<Word2VecSearcher.Match> matches = word2VecSearcher.getMatches(token, 10);
                for (Word2VecSearcher.Match match : matches) {
                    System.out.println(match.match() + " " + match.distance());
                }
            }
        }
    }

}