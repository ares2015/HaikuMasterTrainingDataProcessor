package com.HaikuMasterTrainingDataProcessor.writer;

import com.HaikuMasterTrainingDataProcessor.preprocessor.data.BookData;

import java.io.IOException;
import java.util.List;

/**
 * Created by oliver.eder on 1/23/2017.
 */
public interface TrainingDataWriter {

    void writePreprocessedDataIntoAnalysisFile(List<String> sentences);

    void writePreprocessedDataIntoBooksDirectory(BookData bookData) throws IOException;

    void writeAnalysedData(List<String> trainingDataRow, String outputFilePath) throws IOException;

}
