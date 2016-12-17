package word2vec.analysis;

import org.apache.thrift.TException;
import word2vec.model.Word2VecModel;

import java.io.IOException;

/**
 * Created by Oliver on 12/17/2016.
 */
public interface Word2VecAnalyser {

    Word2VecModel analyseCBOW() throws InterruptedException, IOException, TException;

    Word2VecModel analyseSkipgram() throws InterruptedException, IOException, TException;

}
