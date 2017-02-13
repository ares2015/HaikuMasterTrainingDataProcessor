package com.HaikuMasterTrainingDataProcessor.tagging;

import com.HaikuMasterTrainingDataProcessor.tagging.data.SentenceData;

/**
 * Created by Oliver on 12/17/2016.
 */
public interface PosTagger {

    SentenceData tag(String inputSentence);

}
