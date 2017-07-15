package com.HaikuMasterTrainingDataProcessor.tokenizing;

import java.util.List;

/**
 * Created by oliver.eder on 12/29/2016.
 */
public interface Tokenizer {

    List<String> getTokens(final String sentence);

    boolean containsSpecialChars(String token);

    boolean isLowerCase(String token);

    String removeSpecialCharacters(String token);

    String decapitalize(String token);

}
