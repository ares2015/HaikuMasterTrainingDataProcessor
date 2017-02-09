package com.HaikuMasterTrainingDataProcessor.reader;

import com.HaikuMasterTrainingDataProcessor.preprocessor.data.BookData;
import com.HaikuMasterTrainingDataProcessor.tokenizing.Tokenizer;
import com.HaikuMasterTrainingDataProcessor.tokenizing.TokenizerImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Oliver on 12/28/2016.
 */
public class TextReaderImpl implements TextReader {

    private String inputFilePath = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTextData.txt";

    private Tokenizer tokenizer = new TokenizerImpl();

    @Override
    public BookData readRawData() throws FileNotFoundException {
        List<String> sentences = null;
        String wholeText = "";
        BufferedReader br = null;
        String title = "";
        try {
            br = new BufferedReader(new FileReader("C:\\Users\\Oliver\\Documents\\NlpTrainingData\\TextData.txt"));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String testDataRow = br.readLine();
            while (testDataRow != null) {
                if (testDataRow.contains("Title:")) {
                    String[] split = testDataRow.split("Title:");
                    title += split[1];
                }
                if (testDataRow.contains("Author:")) {
                    String[] split = testDataRow.split("Author:");
                    title += " - " + split[1];
                }
                if (!"".equals(testDataRow)) {
                    wholeText += testDataRow;
                    wholeText += " ";
                    System.out.println(testDataRow);
                }
                testDataRow = br.readLine();
            }
            sentences = Arrays.asList(wholeText.split("[\\.\\!\\?]"));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter("C:\\Users\\Oliver\\Documents\\NlpTrainingData\\TextData.txt");
        pw.close();
        BookData bookData = new BookData(title, sentences);
        return bookData;
    }

    @Override
    public List<String> readPreprocessedData() {
        List<String> sentences = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(inputFilePath));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String sentence = br.readLine();
            while (sentence != null) {
                if (!"".equals(sentence)) {
                    sentence = preprocessSentence(sentence);
                }
                sentences.add(sentence);
                sentence = br.readLine();
            }
        } catch (final IOException e) {
            e.printStackTrace();

        }
        return sentences;
    }

    private String preprocessSentence(String sentence) {
        String[] tokTmp;
        tokTmp = sentence.split("\\ ");
        List<String> preprocessedTokens = new ArrayList<>();
        for (String token : tokTmp) {
            token = tokenizer.removeSpecialCharacters(token);
            preprocessedTokens.add(token);
        }
        final List<String> tokens = removeEmptyStringInSentence(preprocessedTokens);
        return convertListToString(tokens);
    }

    private List<String> removeEmptyStringInSentence(List<String> filteredTokens) {
        final List<String> listTokens = new ArrayList<String>();
        for (final String token : filteredTokens) {
            if (!token.equals("")) {
                listTokens.add(token);
            }
        }
        return listTokens;
    }

    private String convertListToString(List<String> list) {
        String newString = "";
        int i = 0;
        for (String word : list) {
            if (i < list.size() - 1) {
                newString += word + " ";
            } else {
                newString += word;
            }
            i++;
        }
        return newString;
    }

}