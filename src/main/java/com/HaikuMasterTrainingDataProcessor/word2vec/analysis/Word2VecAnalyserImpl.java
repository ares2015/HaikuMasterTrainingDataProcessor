package com.HaikuMasterTrainingDataProcessor.word2vec.analysis;

import com.HaikuMasterTrainingDataProcessor.word2vec.model.Word2VecModel;
import com.HaikuMasterTrainingDataProcessor.word2vec.neuralnetwork.NeuralNetworkType;
import com.HaikuMasterTrainingDataProcessor.word2vec.training.Word2VecTrainerBuilder;
import com.HaikuMasterTrainingDataProcessor.word2vec.util.*;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.thrift.TException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Oliver on 12/17/2016.
 */
public class Word2VecAnalyserImpl implements Word2VecAnalyser {

    private static final Log LOG = AutoLog.getLog();

    private String inputFilePath;

    private String outputFilePath;

    public Word2VecAnalyserImpl(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    @Override
    public Word2VecModel analyseCBOW() throws InterruptedException, IOException, TException {
        File f = new File(inputFilePath);
        if (!f.exists())
            throw new IllegalStateException("Please download and unzip the text8 example from http://mattmahoney.net/dc/text8.zip");
        List<String> read = Common.readToList(f);
        List<List<String>> partitioned = Lists.transform(read, new Function<String, List<String>>() {
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
                .train(partitioned);

        // Writes model to a thrift file
        try (ProfilingTimer timer = ProfilingTimer.create(LOG, "Writing output to file")) {
            FileUtils.writeStringToFile(new File(outputFilePath), ThriftUtils.serializeJson(model.toThrift()));
        }

        // Alternatively, you can write the model to a bin file that's compatible with the C
        // implementation.
        try (final OutputStream os = Files.newOutputStream(Paths.get("text8.bin"))) {
            model.toBinFile(os);
        }

//        interact(model.forSearch());
        return model;
    }

    @Override
    public Word2VecModel analyseSkipgram() throws InterruptedException, IOException, TException {
        List<String> read = Common.readToList(new File(inputFilePath));
        List<List<String>> partitioned = Lists.transform(read, new Function<String, List<String>>() {
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
                .train(partitioned);

        try (ProfilingTimer timer = ProfilingTimer.create(LOG, "Writing output to file")) {
            FileUtils.writeStringToFile(new File(outputFilePath), ThriftUtils.serializeJson(model.toThrift()));
        }
        return model;
    }
}
