package com.HaikuMasterTrainingDataProcessor.tokenizing;

/**
 * Created by oliver.eder on 12/29/2016.
 */
public interface Tokenizer {

    boolean containsSpecialChars(String token);

    boolean isLowerCase(String token);

    String removeSpecialCharacters(String token);

    String decapitalize(String token);

}
