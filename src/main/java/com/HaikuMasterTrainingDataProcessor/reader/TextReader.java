package com.HaikuMasterTrainingDataProcessor.reader;

import com.HaikuMasterTrainingDataProcessor.preprocessor.data.BookData;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by Oliver on 12/28/2016.
 */
public interface TextReader {

    BookData readRawData() throws FileNotFoundException;

    List<String> readPreprocessedData();

}
