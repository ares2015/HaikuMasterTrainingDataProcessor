package com.HaikuMasterTrainingDataProcessor.tagging;

import com.HaikuMasterTrainingDataProcessor.tagging.cache.TagsCache;
import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Oliver on 12/17/2016.
 */
public class PosTaggerImpl implements PosTagger {

    private Properties props;

    private StanfordCoreNLP pipeline;

    public PosTaggerImpl() {
        props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos");
        pipeline = new StanfordCoreNLP(props);
    }

    @Override
    public List<TokenTagData> tag(String inputSentence) {
        List<TokenTagData> tokenTagDataList = new ArrayList<>();
        Annotation annotation = new Annotation(inputSentence);
        pipeline.annotate(annotation);
        if (annotation.get(CoreAnnotations.SentencesAnnotation.class).size() > 0) {
            CoreMap processedSentence = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
            for (CoreLabel token : processedSentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String tag = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                if (TagsCache.tags.contains(tag)) {
                    TokenTagData tokenTagData = new TokenTagData(word, TagsCache.nounTags.contains(tag), TagsCache.adjectiveTags.contains(tag),
                            TagsCache.verbTags.contains(tag), TagsCache.adverbTags.contains(tag));
                    tokenTagDataList.add(tokenTagData);
                }
            }
        }

        return tokenTagDataList;
    }
}