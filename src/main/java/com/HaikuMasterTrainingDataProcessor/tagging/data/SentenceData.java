package com.HaikuMasterTrainingDataProcessor.tagging.data;

import java.util.List;

/**
 * Created by Oliver on 2/13/2017.
 */
public class SentenceData {

    private String taggedSentence;

    private List<TokenTagData> tokenTagDataList;

    public String getTaggedSentence() {
        return taggedSentence;
    }

    public void setTaggedSentence(String taggedSentence) {
        this.taggedSentence = taggedSentence;
    }

    public List<TokenTagData> getTokenTagDataList() {
        return tokenTagDataList;
    }

    public void setTokenTagDataList(List<TokenTagData> tokenTagDataList) {
        this.tokenTagDataList = tokenTagDataList;
    }
}
