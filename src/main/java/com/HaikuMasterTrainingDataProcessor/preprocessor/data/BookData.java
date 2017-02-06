package com.HaikuMasterTrainingDataProcessor.preprocessor.data;

import java.util.List;

/**
 * Created by Oliver on 2/6/2017.
 */
public class BookData {

    private String title;

    private List<String> sentences;

    public BookData(String title, List<String> sentences) {
        this.title = title;
        this.sentences = sentences;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getSentences() {
        return sentences;
    }

}
