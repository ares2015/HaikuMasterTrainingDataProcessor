package com.HaikuMasterTrainingDataProcessor.main;


import com.HaikuMasterTrainingDataProcessor.database.TrainingDataDatabaseAccessor;
import com.HaikuMasterTrainingDataProcessor.preprocessor.TrainingDataPreprocessor;
import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;
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
import java.util.*;

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
    private String outputFilePathTokenTagData = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\TokenTagData.txt";

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
        Map<String, Set<String>> tokenTagDataCache = new HashMap<String, Set<String>>();
        Set<String> vectorDataCache = new HashSet<String>();
        List<String> word2VecTrainingDataRows = new ArrayList<>();
        List<String> tokenTagDataTrainingDataRows = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        int numberOfTaggedWords = 0;
        int numberOfWord2VecWords = 0;
        Word2VecModel word2VecModel = word2VecAnalyser.analyseSkipgram();
        Word2VecSearcher word2VecSearcher = new Word2VecSearcherImpl(word2VecModel);
        List<String> sentences = word2VecModel.getSentences();
        List<List<TokenTagData>> tokenTagDataMultiList = word2VecModel.getTokenTagDataMultiList();
        trainingDataDatabaseAccessor.insertNumberOfSentences(sentences.size());
        for (int i = 0; i < sentences.size(); i++) {
            List<TokenTagData> tokenTagDataList = tokenTagDataMultiList.get(i);
            for (int j = 0; j < tokenTagDataList.size(); j++) {
                String token = tokenTagDataList.get(j).getToken();
                String tag = tokenTagDataList.get(j).getTag();
                if (!tokenizer.isLowerCase(token)) {
                    try {
                        token = tokenizer.decapitalize(token);
                    } catch (StringIndexOutOfBoundsException e) {
                        System.out.println(token);
                    }
                }
                if (tokenTagDataCache.containsKey(token)) {
                    if (!tokenTagDataCache.get(token).contains(tag)) {
                        String tokenTagDataTrainingDataRow = token + "#" + tag;
                        tokenTagDataTrainingDataRows.add(tokenTagDataTrainingDataRow);

                        numberOfTaggedWords++;
                        tokenTagDataCache.get(token).add(tag);
                    }
                } else {
                    Set<String> tags = new HashSet<String>();
                    tags.add(tag);
                    tokenTagDataCache.put(token, tags);
                    String tokenTagDataTrainingDataRow = token + "#" + tag;
                    tokenTagDataTrainingDataRows.add(tokenTagDataTrainingDataRow);
                    numberOfTaggedWords++;
                }
                try {
                    if (!vectorDataCache.contains(token)) {
                        vectorDataCache.add(token);
                        List<Word2VecSearcher.Match> matches = word2VecSearcher.getMatches(token, 310);
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
        trainingDataWriter.writeAnalysedData(tokenTagDataTrainingDataRows, outputFilePathTokenTagData);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(sentences.size() + " sentences processed in " + (elapsedTime / 1000) / 60 + " minutes and "
                + +(elapsedTime / 1000) % 60 + " seconds");
        System.out.println(numberOfTaggedWords + " token tags were added into tags model");
        System.out.println(numberOfWord2VecWords + " tokens were added into word2vec model");
    }
}

