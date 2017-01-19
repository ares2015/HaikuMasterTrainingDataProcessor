package com.HaikuMasterTrainingDataProcessor.word2vec.factory;

import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcher;

import java.util.List;

/**
 * Created by oliver.eder on 1/19/2017.
 */
public interface Word2VecMatchTrainingRowFactory {

    String create(String token, List<Word2VecSearcher.Match> matches);
}
