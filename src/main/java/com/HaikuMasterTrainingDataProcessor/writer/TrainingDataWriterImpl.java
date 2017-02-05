package com.HaikuMasterTrainingDataProcessor.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by oliver.eder on 1/23/2017.
 */
public class TrainingDataWriterImpl implements TrainingDataWriter {

    private BufferedWriter bw = null;

    private FileWriter fw = null;

    private final String FILENAME = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTextData.txt";

    @Override
    public void writePreprocessedData(List<String> sentences) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(FILENAME, true);
            bw = new BufferedWriter(fw);
            for (String sentence : sentences) {
                bw.write(sentence);
                bw.newLine();
            }
            System.out.println("Writing into file finished");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void writeAnalysedData(List<String> trainingDataRows, String outputFilePath) throws IOException {
        fw = new FileWriter(outputFilePath, true);
        bw = new BufferedWriter(fw);
        System.out.println("Starting to write analysed data...");
        for (String trainingDataRow : trainingDataRows) {
            System.out.println(trainingDataRow);
            bw.write(trainingDataRow);
            bw.newLine();
        }
        bw.close();
        fw.close();
    }

}