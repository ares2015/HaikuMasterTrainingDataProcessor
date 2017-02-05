package com.HaikuMasterTrainingDataProcessor.preprocessor.filter;

import com.HaikuMasterTrainingDataProcessor.tokenizing.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2/5/2017.
 */
public class SentenceFilterImpl implements SentenceFilter {

    private Tokenizer tokenizer;

    public SentenceFilterImpl(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }


    @Override
    public String filter(String sentence) {
        String[] tokTmp;
        tokTmp = sentence.split("\\ ");
        List<String> preprocessedTokens = new ArrayList<>();
        for (String token : tokTmp) {
            token = tokenizer.removeSpecialCharacters(token);
            preprocessedTokens.add(token);
        }
        final List<String> tokens = filterEmptyStrings(preprocessedTokens);
        return convertListToString(tokens);
    }

    private List<String> filterEmptyStrings(List<String> filteredTokens) {
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
