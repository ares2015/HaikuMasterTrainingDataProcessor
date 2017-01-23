package com.HaikuMasterTrainingDataProcessor.main;

import com.HaikuMasterTrainingDataProcessor.database.TrainingDataDatabaseAccessor;
import com.HaikuMasterTrainingDataProcessor.tagging.PosTagger;
import com.HaikuMasterTrainingDataProcessor.tagging.PosTaggerImpl;
import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;
import com.HaikuMasterTrainingDataProcessor.word2vec.analysis.Word2VecAnalyser;
import com.HaikuMasterTrainingDataProcessor.word2vec.analysis.Word2VecAnalyserImpl;
import com.HaikuMasterTrainingDataProcessor.word2vec.factory.Word2VecMatchTrainingRowFactory;
import com.HaikuMasterTrainingDataProcessor.word2vec.factory.Word2VecMatchTrainingRowFactoryImpl;
import com.HaikuMasterTrainingDataProcessor.word2vec.model.Word2VecModel;
import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcher;
import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcherImpl;
import com.HaikuMasterTrainingDataProcessor.writer.TrainingDataRowWriter;
import com.HaikuMasterTrainingDataProcessor.writer.TrainingDataRowWriterImpl;
import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import java.io.IOException;
import java.util.*;

/**
 * Created by Oliver on 12/17/2016.
 */
public class HaikuMasterTrainingDataProcessorImpl implements HaikuMasterTrainingDataProcessor {

    private ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    private TrainingDataDatabaseAccessor trainingDataDatabaseAccessor = (TrainingDataDatabaseAccessor) context.getBean("trainingDataDatabaseAccessor");

    private Word2VecAnalyser word2VecAnalyser = new Word2VecAnalyserImpl();
    private PosTagger posTagger = new PosTaggerImpl();
    private Word2VecMatchTrainingRowFactory word2VecMatchTrainingRowFactory = new Word2VecMatchTrainingRowFactoryImpl();
    private TrainingDataRowWriter trainingDataRowWriter = new TrainingDataRowWriterImpl();

    public HaikuMasterTrainingDataProcessorImpl() throws IOException {
    }


    public static void main(String[] args) throws InterruptedException, TException, Word2VecSearcher.UnknownWordException, IOException {
        HaikuMasterTrainingDataProcessor haikuMasterTrainingDataProcessor = new HaikuMasterTrainingDataProcessorImpl();
        haikuMasterTrainingDataProcessor.process();
    }

    @Override
    public void process() throws InterruptedException, TException, IOException {
        Map<String, Set<String>> tokenTagDataCache = new HashMap<String, Set<String>>();
        Set<String> vectorDataCache = new HashSet<String>();
        List<String> trainingDataRows = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        int numberOfTaggedWords = 0;
        int numberOfWord2VecWords = 0;
        Word2VecModel word2VecModel = word2VecAnalyser.analyseCBOW();
        Word2VecSearcher word2VecSearcher = new Word2VecSearcherImpl(word2VecModel);
        Iterable<String> sentences = word2VecModel.getVocab();
        for (String sentence : sentences) {
            List<TokenTagData> tokenTagDataList = posTagger.tag(sentence);
            numberOfTaggedWords += tokenTagDataList.size();
            for (TokenTagData tokenTagData : tokenTagDataList) {
                List<Word2VecSearcher.Match> matches = null;
                try {
                    String token = tokenTagData.getToken();
                    String tag = tokenTagData.getTag();
                    if (tokenTagDataCache.containsKey(token)) {
                        if (!tokenTagDataCache.get(token).contains(tag)) {
                            //                        trainingDataDatabaseAccessor.insertTokenTagData(tokenTagData);
                            numberOfTaggedWords++;
                            tokenTagDataCache.get(token).add(tag);
                        }
                    } else {
                        Set<String> tags = new HashSet<String>();
                        tags.add(tag);
                        tokenTagDataCache.put(token, tags);
//                        trainingDataDatabaseAccessor.insertTokenTagData(tokenTagData);
                        numberOfTaggedWords++;
                    }
                    try {
                        if (!vectorDataCache.contains(token)) {
                            vectorDataCache.add(token);
                            matches = word2VecSearcher.getMatches(token, 11);
                            String trainingDataRow = word2VecMatchTrainingRowFactory.create(token, matches);
                            trainingDataRows.add(trainingDataRow);
                            numberOfWord2VecWords++;
                        }

                    } catch (Word2VecSearcher.UnknownWordException e) {
                        e.printStackTrace();
                    }
                    if (matches != null) {

                    }
                } catch (CannotGetJdbcConnectionException ex) {
                    System.out.println(ex);
                }
            }
        }
        trainingDataRowWriter.write(trainingDataRows);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Data processed in " + (elapsedTime / 1000) + " seconds / " + (elapsedTime / 1000) / 60 + " minutes");
        System.out.println(numberOfTaggedWords + " token tags were added into tags model");
        System.out.println(numberOfWord2VecWords + " tokens were added into word2vec model");
    }

}