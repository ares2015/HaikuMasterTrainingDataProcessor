package com.HaikuMasterTrainingDataProcessor.tokens;

import java.util.List;

/**
 * Created by Oliver on 12/16/2016.
 */
public interface Tokenizer {

    String removeBrackets(String string, char bracket1, char bracket2);

    String removeDoubleQuotes(String string);

    String removeEmptyStrings(String string);

    public List<String> getTokens(final String sentence);

}
