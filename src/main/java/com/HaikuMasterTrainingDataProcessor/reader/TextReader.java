package com.HaikuMasterTrainingDataProcessor.reader;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by Oliver on 12/28/2016.
 */
public interface TextReader {

    List<String> readRawData() throws FileNotFoundException;

    List<String> readPreprocessedData();

}
