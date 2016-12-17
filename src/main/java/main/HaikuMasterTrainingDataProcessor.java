package main;

import org.apache.thrift.TException;
import word2vec.search.Word2VecSearcher;

import java.io.IOException;

/**
 * Created by Oliver on 12/17/2016.
 */
public interface HaikuMasterTrainingDataProcessor {

    void process() throws InterruptedException, TException, IOException, Word2VecSearcher.UnknownWordException;

}
