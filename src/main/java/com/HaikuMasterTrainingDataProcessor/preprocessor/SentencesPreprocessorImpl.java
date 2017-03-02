package com.HaikuMasterTrainingDataProcessor.preprocessor;

import com.HaikuMasterTrainingDataProcessor.tagging.PosTagger;
import com.HaikuMasterTrainingDataProcessor.tagging.data.SentenceData;
import com.HaikuMasterTrainingDataProcessor.tagging.data.SentencesData;
import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2/13/2017.
 */
public class SentencesPreprocessorImpl implements SentencesPreprocessor {

    private PosTagger posTagger;

    public SentencesPreprocessorImpl(PosTagger posTagger) {
        this.posTagger = posTagger;
    }

    @Override
    public SentencesData preprocess() {
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
                String preprocessedSentence = "";
                if (!"".equals(sentence)) {
                    SentenceData sentenceData = posTagger.tag(sentence);
                    String taggedSentence = sentenceData.getTaggedSentence();
                    if (!"".equals(taggedSentence)) {
                        sentences.add(preprocessedSentence);
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

}