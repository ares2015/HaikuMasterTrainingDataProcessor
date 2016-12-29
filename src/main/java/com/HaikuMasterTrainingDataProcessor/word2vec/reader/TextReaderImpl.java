package com.HaikuMasterTrainingDataProcessor.word2vec.reader;

import com.HaikuMasterTrainingDataProcessor.tokenizing.Tokenizer;
import com.HaikuMasterTrainingDataProcessor.tokenizing.TokenizerImpl;
import com.HaikuMasterTrainingDataProcessor.word2vec.util.StopWordsCache;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 12/28/2016.
 */
public class TextReaderImpl implements TextReader {

    //    private String inputFilePath = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTextData.txt";
    private String inputFilePath = "C:\\Users\\oliver.eder\\Downloads\\books\\HaikuMasterTextData.txt";

    private Tokenizer tokenizer = new TokenizerImpl();

    public List<String> readText() {
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
                    sentence = removeStopWordsFromSentence(sentence);
                }
                sentences.add(sentence);
                sentence = br.readLine();
            }
        } catch (final IOException e) {
            e.printStackTrace();

        }
        return sentences;
    }

    private String removeStopWordsFromSentence(String sentence) {
        String[] tokTmp;
        tokTmp = sentence.split("\\ ");
        List<String> filteredTokens = new ArrayList<>();
        for (String token : tokTmp) {
            if (tokenizer.containsSpecialChars(token)) {
                token = tokenizer.removeSpecialCharacters(token);
            }
            try {
                if (!tokenizer.isLowerCase(token)) {
                    token = tokenizer.decapitalize(token);
                }
            }catch (StringIndexOutOfBoundsException e){
                System.out.println(token);
            }
            if (!(StopWordsCache.stopWordsCache.contains(token))) {
                filteredTokens.add(token);
            }
        }
        final List<String> tokens = removeEmptyStringInSentence(filteredTokens);
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