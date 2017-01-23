package com.HaikuMasterTrainingDataProcessor.writer;

import java.io.IOException;
import java.util.List;

/**
 * Created by oliver.eder on 1/23/2017.
 */
public interface TrainingDataRowWriter {

    void write(List<String> trainingDataRow) throws IOException;

}
