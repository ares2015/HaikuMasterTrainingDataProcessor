package preprocessing;

import tokens.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 12/16/2016.
 */
public class SentencePreprocessorImpl implements SentencesPreprocessor {

    private Tokenizer tokenizer;

    public SentencePreprocessorImpl(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public List<String> preprocess(List<String> sentences) {
        List<String> processedSentences = new ArrayList<String>();
        for (String sentence : sentences) {
            if (sentence.contains("(")) {
                sentence = tokenizer.removeBrackets(sentence, '(', ')');
            }
            if (sentence.contains("[")) {
                sentence = tokenizer.removeBrackets(sentence, '[', ']');
            }
            if (sentence.contains("{")) {
                sentence = tokenizer.removeBrackets(sentence, '{', '}');
            }
            if (sentence.contains("\"")) {
                sentence = tokenizer.removeDoubleQuotes(sentence);
            }
            sentence = tokenizer.removeEmptyStrings(sentence);
            processedSentences.add(sentence);
            System.out.println(sentence);
        }
        return processedSentences;
    }
}
