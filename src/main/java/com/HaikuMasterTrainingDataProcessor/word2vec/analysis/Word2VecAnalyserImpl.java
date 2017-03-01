package com.HaikuMasterTrainingDataProcessor.word2vec.analysis;

import com.HaikuMasterTrainingDataProcessor.processor.SentencesProcessor;
import com.HaikuMasterTrainingDataProcessor.tagging.data.SentencesData;
import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;
import com.HaikuMasterTrainingDataProcessor.word2vec.model.Word2VecModel;
import com.HaikuMasterTrainingDataProcessor.word2vec.neuralnetwork.NeuralNetworkType;
import com.HaikuMasterTrainingDataProcessor.word2vec.training.Word2VecTrainerBuilder;
import com.HaikuMasterTrainingDataProcessor.word2vec.util.Format;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.thrift.TException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Oliver on 12/17/2016.
 */
public class Word2VecAnalyserImpl implements Word2VecAnalyser {

    private SentencesProcessor sentencesProcessor;

    public Word2VecAnalyserImpl(SentencesProcessor sentencesProcessor) {
        this.sentencesProcessor = sentencesProcessor;
    }

    @Override
    public Word2VecModel analyseCBOW() throws InterruptedException, IOException, TException {
        SentencesData sentencesData = sentencesProcessor.process();
        List<String> sentences = sentencesData.getSentences();
        List<List<TokenTagData>> tokenTagDataMultiList = sentencesData.getTokenTagDataMultiList();
        List<List<String>> tokens = Lists.transform(sentences, new Function<String, List<String>>() {
            @Override
            public List<String> apply(String input) {
                return Arrays.asList(input.split(" "));
            }
        });

        Word2VecModel model = Word2VecModel.trainer()
                .setMinVocabFrequency(20)
                .useNumThreads(20)
                .setWindowSize(5)
                .type(NeuralNetworkType.CBOW)
                .setLayerSize(310)
                .useHierarchicalSoftmax()
                .useNegativeSamples(25)
                .setDownSamplingRate(1e-4)
                .setNumIterations(15)
                .setListener(new Word2VecTrainerBuilder.TrainingProgressListener() {
                    @Override
                    public void update(Stage stage, double progress) {
                        System.out.println(String.format("%s is %.2f%% complete", Format.formatEnum(stage), progress * 100));
                    }
                })
                .train(tokens);
        model.setSentences(sentences);
        model.setTokenTagDataMultiList(tokenTagDataMultiList);
        return model;
    }

    @Override
    public Word2VecModel analyseSkipgram() throws InterruptedException, IOException, TException {
        SentencesData sentencesData = sentencesProcessor.process();
        List<String> sentences = sentencesData.getSentences();
        List<List<TokenTagData>> tokenTagDataMultiList = sentencesData.getTokenTagDataMultiList();
        List<List<String>> tokens = Lists.transform(sentences, new Function<String, List<String>>() {
            @Override
            public List<String> apply(String input) {
                return Arrays.asList(input.split(" "));
            }
        });

        Word2VecModel model = Word2VecModel.trainer()
                .setMinVocabFrequency(20)
                .useNumThreads(20)
                .setWindowSize(5)
                .type(NeuralNetworkType.SKIP_GRAM)
                .useHierarchicalSoftmax()
                .setLayerSize(310)
                .useNegativeSamples(5)
                .setDownSamplingRate(1e-3)
                .setNumIterations(15)
                .setListener(new Word2VecTrainerBuilder.TrainingProgressListener() {
                    @Override
                    public void update(Word2VecTrainerBuilder.TrainingProgressListener.Stage stage, double progress) {
                        System.out.println(String.format("%s is %.2f%% complete", Format.formatEnum(stage), progress * 100));
                    }
                })
                .train(tokens);
        model.setSentences(sentences);
        model.setTokenTagDataMultiList(tokenTagDataMultiList);
        return model;
    }
}