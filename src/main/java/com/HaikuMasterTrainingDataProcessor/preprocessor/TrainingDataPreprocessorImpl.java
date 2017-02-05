package com.HaikuMasterTrainingDataProcessor.preprocessor;

import com.HaikuMasterTrainingDataProcessor.preprocessor.filter.SentenceFilter;
import com.HaikuMasterTrainingDataProcessor.reader.TextReader;
import com.HaikuMasterTrainingDataProcessor.writer.TrainingDataWriter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2/5/2017.
 */
public class TrainingDataPreprocessorImpl implements TrainingDataPreprocessor {

    private TextReader textReader;

    private SentenceFilter sentenceFilter;

    private TrainingDataWriter trainingDataWriter;

    public TrainingDataPreprocessorImpl(TextReader textReader, SentenceFilter sentenceFilter,
                                        TrainingDataWriter trainingDataWriter) {
        this.textReader = textReader;
        this.sentenceFilter = sentenceFilter;
        this.trainingDataWriter = trainingDataWriter;
    }

    @Override
    public void preprocess() throws FileNotFoundException {
        List<String> sentences = textReader.readRawData();
        List<String> preprocessedSentences = new ArrayList<>();
        for (String sentence : sentences) {
            String preprocessedSentence = sentenceFilter.filter(sentence);
            preprocessedSentences.add(preprocessedSentence);
        }
        trainingDataWriter.writePreprocessedData(preprocessedSentences);
    }


}
