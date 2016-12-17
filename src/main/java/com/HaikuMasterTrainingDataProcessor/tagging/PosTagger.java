package com.HaikuMasterTrainingDataProcessor.tagging;

import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;

import java.util.List;

/**
 * Created by Oliver on 12/17/2016.
 */
public interface PosTagger {

    List<TokenTagData> tag(String inputSentence);

}
