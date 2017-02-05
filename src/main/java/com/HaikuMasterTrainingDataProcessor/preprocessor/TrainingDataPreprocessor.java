package com.HaikuMasterTrainingDataProcessor.preprocessor;

import java.io.FileNotFoundException;

/**
 * Created by Oliver on 2/5/2017.
 */
public interface TrainingDataPreprocessor {

    void preprocess() throws FileNotFoundException;
}
