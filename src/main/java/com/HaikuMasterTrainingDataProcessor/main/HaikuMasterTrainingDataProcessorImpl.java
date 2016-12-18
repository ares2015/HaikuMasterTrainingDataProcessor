package com.HaikuMasterTrainingDataProcessor.main;

import com.HaikuMasterTrainingDataProcessor.database.TrainingDataDatabaseAccessor;
import com.HaikuMasterTrainingDataProcessor.tagging.PosTagger;
import com.HaikuMasterTrainingDataProcessor.tagging.PosTaggerImpl;
import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;
import com.HaikuMasterTrainingDataProcessor.word2vec.analysis.Word2VecAnalyser;
import com.HaikuMasterTrainingDataProcessor.word2vec.analysis.Word2VecAnalyserImpl;
import com.HaikuMasterTrainingDataProcessor.word2vec.model.Word2VecModel;
import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcher;
import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcherImpl;
import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.List;

/**
 * Created by Oliver on 12/17/2016.
 */
public class HaikuMasterTrainingDataProcessorImpl implements HaikuMasterTrainingDataProcessor {

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");
    TrainingDataDatabaseAccessor trainingDataDatabaseAccessor = (TrainingDataDatabaseAccessor) context.getBean("trainingDataDatabaseAccessor");

    String inputFilePath = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTextData.txt";
    String outputFilePath = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\Word2Vec.txt";
    Word2VecAnalyser word2VecAnalyser = new Word2VecAnalyserImpl(inputFilePath, outputFilePath);
    private PosTagger posTagger = new PosTaggerImpl();

    public static void main(String[] args) throws InterruptedException, TException, Word2VecSearcher.UnknownWordException, IOException {
        HaikuMasterTrainingDataProcessor haikuMasterTrainingDataProcessor = new HaikuMasterTrainingDataProcessorImpl();
        haikuMasterTrainingDataProcessor.process();
    }

    @Override
    public void process() throws InterruptedException, TException, IOException {
        long startTime = System.currentTimeMillis();
        int numberOfTaggedWords = 0;
        Word2VecModel word2VecModel = word2VecAnalyser.analyseCBOW();
        Word2VecSearcher word2VecSearcher = new Word2VecSearcherImpl(word2VecModel);
        Iterable<String> vocab = word2VecModel.getVocab();
        for (String sentence : vocab) {
            List<TokenTagData> tokenTagDataList = posTagger.tag(sentence);
            numberOfTaggedWords += tokenTagDataList.size();
            for (TokenTagData tokenTagData : tokenTagDataList) {
                trainingDataDatabaseAccessor.insertTokenTagData(tokenTagData);
                List<Word2VecSearcher.Match> matches = null;
                try {
                    matches = word2VecSearcher.getMatches(tokenTagData.getToken(), 10);
                } catch (Word2VecSearcher.UnknownWordException e) {
                    e.printStackTrace();
                }
//                for (Word2VecSearcher.Match match : matches) {
//                    System.out.println(match.match() + " " + match.distance());
//                }
                if (matches != null) {
                    trainingDataDatabaseAccessor.insertTokenWord2VecData(tokenTagData.getToken(), matches);
                }

            }
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Data processed in " + elapsedTime / 1000 + " seconds");
        System.out.println(numberOfTaggedWords + " were tagged and added into model");
    }

}