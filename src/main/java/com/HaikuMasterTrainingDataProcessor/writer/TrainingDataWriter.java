package com.HaikuMasterTrainingDataProcessor.writer;

import java.io.IOException;
import java.util.List;

/**
 * Created by oliver.eder on 1/23/2017.
 */
public interface TrainingDataWriter {

    void writePreprocessedData(List<String> sentences);

    void writeAnalysedData(List<String> trainingDataRow, String outputFilePath) throws IOException;

}
