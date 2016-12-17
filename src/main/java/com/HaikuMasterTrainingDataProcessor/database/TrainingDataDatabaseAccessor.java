package com.HaikuMasterTrainingDataProcessor.database;

import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcher;

import java.util.List;

/**
 * Created by Oliver on 12/17/2016.
 */
public interface TrainingDataDatabaseAccessor {

    void insertTokenWord2VecData(String token, List<Word2VecSearcher.Match> matches);

}
