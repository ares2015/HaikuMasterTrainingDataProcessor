package com.HaikuMasterTrainingDataProcessor.reader;

import com.HaikuMasterTrainingDataProcessor.preprocessor.data.BookData;

import java.io.FileNotFoundException;

/**
 * Created by Oliver on 12/28/2016.
 */
public interface BookTextReader {

    BookData readRawData() throws FileNotFoundException;

}
