package com.HaikuMasterTrainingDataProcessor.main;


import com.HaikuMasterTrainingDataProcessor.database.TrainingDataDatabaseAccessor;
import com.HaikuMasterTrainingDataProcessor.preprocessor.TrainingDataPreprocessor;
import com.HaikuMasterTrainingDataProcessor.tokenizing.Tokenizer;
import com.HaikuMasterTrainingDataProcessor.word2vec.analysis.Word2VecAnalyser;
import com.HaikuMasterTrainingDataProcessor.word2vec.factory.Word2VecMatchTrainingRowFactory;
import com.HaikuMasterTrainingDataProcessor.word2vec.model.Word2VecModel;
import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcher;
import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcherImpl;
import com.HaikuMasterTrainingDataProcessor.writer.TrainingDataWriter;
import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Oliver on 12/17/2016.
 */
public class HaikuMasterTrainingDataProcessorImpl implements HaikuMasterTrainingDataProcessor {

    private TrainingDataDatabaseAccessor trainingDataDatabaseAccessor;

    private TrainingDataPreprocessor trainingDataPreprocessor;

    private Tokenizer tokenizer;

    private TrainingDataWriter trainingDataWriter;

    private Word2VecMatchTrainingRowFactory word2VecMatchTrainingRowFactory;

    private Word2VecAnalyser word2VecAnalyser;

    private String outputFilePathWord2Vec = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\Word2VecModelData.txt";

    public HaikuMasterTrainingDataProcessorImpl(TrainingDataDatabaseAccessor trainingDataDatabaseAccessor, TrainingDataPreprocessor trainingDataPreprocessor, Tokenizer tokenizer,
                                                TrainingDataWriter trainingDataWriter, Word2VecMatchTrainingRowFactory word2VecMatchTrainingRowFactory,
                                                Word2VecAnalyser word2VecAnalyser) {
        this.trainingDataDatabaseAccessor = trainingDataDatabaseAccessor;
        this.trainingDataPreprocessor = trainingDataPreprocessor;
        this.tokenizer = tokenizer;
        this.trainingDataWriter = trainingDataWriter;

        this.word2VecMatchTrainingRowFactory = word2VecMatchTrainingRowFactory;
        this.word2VecAnalyser = word2VecAnalyser;
    }

    public static void main(String[] args) throws InterruptedException, TException, Word2VecSearcher.UnknownWordException, IOException {
        final ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        final HaikuMasterTrainingDataProcessor haikuMasterTrainingDataProcessor = (HaikuMasterTrainingDataProcessor) context.getBean("haikuMasterTrainingDataProcessor");
        haikuMasterTrainingDataProcessor.preprocess();
        haikuMasterTrainingDataProcessor.process();
    }

    @Override
    public void preprocess() throws IOException {
        trainingDataPreprocessor.preprocess();
    }

    @Override
    public void process() throws InterruptedException, TException, IOException {
        Set<String> vectorDataCache = new HashSet<String>();
        List<String> word2VecTrainingDataRows = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        int numberOfWord2VecWords = 0;
        Word2VecModel word2VecModel = word2VecAnalyser.analyseSkipgram();
        Word2VecSearcher word2VecSearcher = new Word2VecSearcherImpl(word2VecModel);
        List<String> sentences = word2VecModel.getSentences();
        trainingDataDatabaseAccessor.insertNumberOfSentences(sentences.size());
        for (int i = 0; i < sentences.size(); i++) {
            String sentence = sentences.get(i);
            List<String> tokens = tokenizer.getTokens(sentence);
            for (int j = 0; j < tokens.size(); j++) {
                String token = tokens.get(j);
                try {
                    if (!tokenizer.isLowerCase(token)) {
                        token = tokenizer.decapitalize(token);
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println(token);
                }
                try {
                    if (!vectorDataCache.contains(token)) {
                        vectorDataCache.add(token);
                        List<Word2VecSearcher.Match> matches = word2VecSearcher.getMatches(token, 200);
                        String trainingDataRow = word2VecMatchTrainingRowFactory.create(token, matches);
                        System.out.println(trainingDataRow);
                        word2VecTrainingDataRows.add(trainingDataRow);
                        numberOfWord2VecWords++;
                    }
                } catch (Word2VecSearcher.UnknownWordException e) {
                }
            }
        }
        trainingDataWriter.writeAnalysedData(word2VecTrainingDataRows, outputFilePathWord2Vec);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(sentences.size() + " sentences processed in " + (elapsedTime / 1000) / 60 + " minutes and "
                + +(elapsedTime / 1000) % 60 + " seconds");
        System.out.println(numberOfWord2VecWords + " tokens were added into word2vec model");
    }
}