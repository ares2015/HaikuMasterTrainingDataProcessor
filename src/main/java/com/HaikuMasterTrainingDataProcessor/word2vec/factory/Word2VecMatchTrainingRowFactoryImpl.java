package com.HaikuMasterTrainingDataProcessor.word2vec.factory;

import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcher;

import java.util.List;

/**
 * Created by oliver.eder on 1/19/2017.
 */
public class Word2VecMatchTrainingRowFactoryImpl implements Word2VecMatchTrainingRowFactory {

    @Override
    public String create(String token, List<Word2VecSearcher.Match> matches) {
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(token).append("#");
        for (Word2VecSearcher.Match match : matches) {
            if (i > 0) {
                stringBuilder.append(match.match()).append("*").append(match.distance()).append("@");
            }
            i++;
        }
        return stringBuilder.toString();
    }


}
