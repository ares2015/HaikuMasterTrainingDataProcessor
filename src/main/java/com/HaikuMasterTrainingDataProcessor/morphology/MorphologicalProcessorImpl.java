package com.HaikuMasterTrainingDataProcessor.morphology;

/**
 * Created by oled on 3/2/2017.
 */
public class MorphologicalProcessorImpl implements MorphologicalProcessor {

    @Override
    public String removeSuffix(String token) {
        if (token.endsWith("ing")) {
            return token.substring(0, token.length() - 3);
        }
        if (token.endsWith("ed")) {
            return token.substring(0, token.length() - 2);
        }
        return token;
    }
}
