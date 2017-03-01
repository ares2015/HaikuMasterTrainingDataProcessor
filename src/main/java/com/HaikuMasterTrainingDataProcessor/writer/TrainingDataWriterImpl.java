package com.HaikuMasterTrainingDataProcessor.writer;

import com.HaikuMasterTrainingDataProcessor.preprocessor.data.BookData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by oliver.eder on 1/23/2017.
 */
public class TrainingDataWriterImpl implements TrainingDataWriter {

    private BufferedWriter bw = null;

    private FileWriter fw = null;

    private final String FILENAME = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\HaikuMasterTextData.txt";

    @Override
    public void writePreprocessedDataIntoAnalysisFile(List<String> sentences) {
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
    public void writePreprocessedDataIntoBooksDirectory(BookData bookData) throws IOException {
        File file = new File("C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\books\\" + bookData.getTitle() + ".txt");
        file.createNewFile();
        file.getAbsolutePath();
        BufferedWriter bw = null;
        FileWriter fw = null;
        List<String> sentences = bookData.getSentences();
        try {
            fw = new FileWriter(file.getAbsolutePath(), true);
            bw = new BufferedWriter(fw);
            for (String sentence : sentences) {
                bw.write(sentence);
                bw.newLine();
            }
            System.out.println("Writing into book directory finished");
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