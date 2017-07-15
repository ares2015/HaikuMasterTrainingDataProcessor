package com.HaikuMasterTrainingDataProcessor.reader;

import java.io.IOException;
import java.util.List;

/**
 * Created by Oliver on 7/15/2017.
 */
public interface FilteredHaikuMasterTextDataReader {

    List<String> read() throws IOException;

}
