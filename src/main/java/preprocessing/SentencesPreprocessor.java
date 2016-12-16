package preprocessing;

import java.util.List;

/**
 * Created by Oliver on 12/16/2016.
 */
public interface SentencesPreprocessor {

    List<String> preprocess(List<String> sentences);

}
