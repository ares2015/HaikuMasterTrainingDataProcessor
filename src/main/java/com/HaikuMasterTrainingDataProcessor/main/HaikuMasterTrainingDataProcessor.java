package com.HaikuMasterTrainingDataProcessor.main;

import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcher;
import org.apache.thrift.TException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Oliver on 12/17/2016.
 */
public interface HaikuMasterTrainingDataProcessor {

    void preprocess() throws FileNotFoundException;

    void process() throws InterruptedException, TException, IOException, Word2VecSearcher.UnknownWordException;

}
