package com.HaikuMasterTrainingDataProcessor.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by oliver.eder on 1/23/2017.
 */
public class TrainingDataRowWriterImpl implements TrainingDataRowWriter {

    private String outputFilePath = "C:\\Users\\oliver.eder\\Downloads\\books\\Word2VecModelData.txt";

    private BufferedWriter bw = null;

    private FileWriter fw = null;

    @Override
    public void write(List<String> trainingDataRows) throws IOException {
        fw = new FileWriter(outputFilePath, true);
        bw = new BufferedWriter(fw);
        System.out.println("Starting to write data...");
        for (String trainingDataRow : trainingDataRows) {
            System.out.println(trainingDataRow);
            bw.write(trainingDataRow);
            bw.newLine();
        }
        bw.close();
        fw.close();
    }

}