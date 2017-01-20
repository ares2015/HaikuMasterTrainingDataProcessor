package com.HaikuMasterTrainingDataProcessor.tagging.data;

/**
 * Created by Oliver on 12/17/2016.
 */
public class TokenTagData {

    private String token;

    private boolean isNoun;

    private boolean isAdjective;

    private boolean isVerb;

    private boolean isAdverb;

    private String tag;

    public TokenTagData(String token, boolean isNoun, boolean isAdjective, boolean isVerb, boolean isAdverb, String tag) {
        this.token = token;
        this.isNoun = isNoun;
        this.isAdjective = isAdjective;
        this.isVerb = isVerb;
        this.isAdverb = isAdverb;
        this.tag = tag;
    }

    public String getToken() {
        return token;
    }

    public boolean isNoun() {
        return isNoun;
    }

    public boolean isAdjective() {
        return isAdjective;
    }

    public boolean isVerb() {
        return isVerb;
    }

    public boolean isAdverb() {
        return isAdverb;
    }

    public String getTag() {
        return tag;
    }
}
