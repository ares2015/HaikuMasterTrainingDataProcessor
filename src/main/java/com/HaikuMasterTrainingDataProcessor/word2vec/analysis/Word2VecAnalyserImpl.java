package com.HaikuMasterTrainingDataProcessor.word2vec.analysis;

import com.HaikuMasterTrainingDataProcessor.reader.TextReader;
import com.HaikuMasterTrainingDataProcessor.reader.TextReaderImpl;
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

    private TextReader textReader = new TextReaderImpl();

    @Override
    public Word2VecModel analyseCBOW() throws InterruptedException, IOException, TException {
        List<String> sentences = textReader.readText();
        List<List<String>> tokens = Lists.transform(sentences, new Function<String, List<String>>() {
            @Override
            public List<String> apply(String input) {
                return Arrays.asList(input.split(" "));
            }
        });

        Word2VecModel model = Word2VecModel.trainer()
                .setMinVocabFrequency(5)
                .useNumThreads(20)
                .setWindowSize(8)
                .type(NeuralNetworkType.CBOW)
                .setLayerSize(200)
                .useNegativeSamples(25)
                .setDownSamplingRate(1e-4)
                .setNumIterations(5)
                .setListener(new Word2VecTrainerBuilder.TrainingProgressListener() {
                    @Override
                    public void update(Stage stage, double progress) {
                        System.out.println(String.format("%s is %.2f%% complete", Format.formatEnum(stage), progress * 100));
                    }
                })
                .train(tokens);
        model.setSentences(sentences);
        return model;
    }

    @Override
    public Word2VecModel analyseSkipgram() throws InterruptedException, IOException, TException {
        List<String> sentences = textReader.readText();
        List<List<String>> tokens = Lists.transform(sentences, new Function<String, List<String>>() {
            @Override
            public List<String> apply(String input) {
                return Arrays.asList(input.split(" "));
            }
        });

        Word2VecModel model = Word2VecModel.trainer()
                .setMinVocabFrequency(5)
                .useNumThreads(20)
                .setWindowSize(7)
                .type(NeuralNetworkType.SKIP_GRAM)
                .useHierarchicalSoftmax()
                .setLayerSize(300)
                .useNegativeSamples(0)
                .setDownSamplingRate(1e-3)
                .setNumIterations(5)
                .setListener(new Word2VecTrainerBuilder.TrainingProgressListener() {
                    @Override
                    public void update(Word2VecTrainerBuilder.TrainingProgressListener.Stage stage, double progress) {
                        System.out.println(String.format("%s is %.2f%% complete", Format.formatEnum(stage), progress * 100));
                    }
                })
                .train(tokens);

        return model;
    }
}
