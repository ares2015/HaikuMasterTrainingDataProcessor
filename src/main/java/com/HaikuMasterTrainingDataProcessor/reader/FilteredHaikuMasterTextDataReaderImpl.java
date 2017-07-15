package com.HaikuMasterTrainingDataProcessor.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 7/15/2017.
 */
public class FilteredHaikuMasterTextDataReaderImpl implements FilteredHaikuMasterTextDataReader {

    @Override
    public List<String> read() throws IOException {
        List<String> sentences = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\FilteredHaikuMasterTextData.txt"));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String sentence = br.readLine();
            while (sentence != null) {
                if (!"".equals(sentence)) {
                    sentences.add(sentence);
                }
                sentence = br.readLine();
            }

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return sentences;
    }

}