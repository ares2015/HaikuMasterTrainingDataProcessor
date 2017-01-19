package com.HaikuMasterTrainingDataProcessor.database;

import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;

/**
 * Created by Oliver on 12/17/2016.
 */
public interface TrainingDataDatabaseAccessor {

    void insertTokenTagData(TokenTagData tokenTagData);

}
