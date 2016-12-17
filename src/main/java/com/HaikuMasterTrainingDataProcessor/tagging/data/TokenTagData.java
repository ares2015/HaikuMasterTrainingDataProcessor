package com.HaikuMasterTrainingDataProcessor.tagging.data;

/**
 * Created by Oliver on 12/17/2016.
 */
public class TokenTagData {

    private String token;

    private String tag;

    public TokenTagData(String token, String tag) {
        this.token = token;
        this.tag = tag;
    }

    public String getToken() {
        return token;
    }

    public String getTag() {
        return tag;
    }

}
