package com.HaikuMasterTrainingDataProcessor.processor;

import com.HaikuMasterTrainingDataProcessor.tagging.PosTagger;
import com.HaikuMasterTrainingDataProcessor.tagging.data.SentenceData;
import com.HaikuMasterTrainingDataProcessor.tagging.data.SentencesData;
import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;
import com.HaikuMasterTrainingDataProcessor.tokenizing.Tokenizer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2/13/2017.
 */
public class SentencesProcessorImpl implements SentencesProcessor {

    private Tokenizer tokenizer;

    private PosTagger posTagger;

    public SentencesProcessorImpl(Tokenizer tokenizer, PosTagger posTagger) {
        this.tokenizer = tokenizer;
        this.posTagger = posTagger;
    }

    @Override
    public SentencesData process() {
        List<List<TokenTagData>> tokenTagDataMultiList = new ArrayList<>();
        List<String> sentences = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\HaikuMasterTextData.txt"));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Entering sentence preprocessor...");
            String sentence = br.readLine();
            while (sentence != null) {
                String processedSentence = "";
                if (!"".equals(sentence)) {
                    SentenceData sentenceData = posTagger.tag(sentence);
                    String taggedSentence = sentenceData.getTaggedSentence();
                    if (!"".equals(taggedSentence)) {
                        processedSentence = processSentence(taggedSentence);
                        sentences.add(processedSentence);
                        tokenTagDataMultiList.add(sentenceData.getTokenTagDataList());
                        System.out.println("Tagging sentence: " + sentence);
                    }
                }
                sentence = br.readLine();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return new SentencesData(sentences, tokenTagDataMultiList);
    }

    private String processSentence(String sentence) {
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