package com.HaikuMasterTrainingDataProcessor.preprocessor;

import com.HaikuMasterTrainingDataProcessor.preprocessor.data.BookData;
import com.HaikuMasterTrainingDataProcessor.preprocessor.filter.SentenceFilter;
import com.HaikuMasterTrainingDataProcessor.reader.BookTextReader;
import com.HaikuMasterTrainingDataProcessor.writer.TrainingDataWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2/5/2017.
 */
public class TrainingDataPreprocessorImpl implements TrainingDataPreprocessor {

    private BookTextReader bookTextReader;

    private SentenceFilter sentenceFilter;

    private TrainingDataWriter trainingDataWriter;

    public TrainingDataPreprocessorImpl(BookTextReader bookTextReader, SentenceFilter sentenceFilter,
                                        TrainingDataWriter trainingDataWriter) {
        this.bookTextReader = bookTextReader;
        this.sentenceFilter = sentenceFilter;
        this.trainingDataWriter = trainingDataWriter;
    }

    @Override
    public void preprocess() throws IOException {
        BookData bookData = bookTextReader.readRawData();
        List<String> sentences = bookData.getSentences();
        List<String> preprocessedSentences = new ArrayList<>();
        for (String sentence : sentences) {
            String preprocessedSentence = sentenceFilter.filter(sentence);
            preprocessedSentences.add(preprocessedSentence);
        }
        trainingDataWriter.writePreprocessedDataIntoBooksDirectory(bookData);
        trainingDataWriter.writePreprocessedDataIntoAnalysisFile(preprocessedSentences);
    }

}