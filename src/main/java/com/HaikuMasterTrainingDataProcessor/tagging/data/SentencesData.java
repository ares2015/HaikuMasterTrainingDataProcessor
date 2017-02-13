package com.HaikuMasterTrainingDataProcessor.tagging.data;

import java.util.List;

/**
 * Created by Oliver on 2/13/2017.
 */
public class SentencesData {

    private List<String> sentences;

    private List<List<TokenTagData>> tokenTagDataMultiList;

    public SentencesData(List<String> sentences, List<List<TokenTagData>> tokenTagDataMultiList) {
        this.sentences = sentences;
        this.tokenTagDataMultiList = tokenTagDataMultiList;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public List<List<TokenTagData>> getTokenTagDataMultiList() {
        return tokenTagDataMultiList;
    }

}