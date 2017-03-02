package com.HaikuMasterTrainingDataProcessor.tagging;

import com.HaikuMasterTrainingDataProcessor.morphology.MorphologicalProcessor;
import com.HaikuMasterTrainingDataProcessor.morphology.MorphologicalProcessorImpl;
import com.HaikuMasterTrainingDataProcessor.tagging.cache.TagsCache;
import com.HaikuMasterTrainingDataProcessor.tagging.data.SentenceData;
import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;
import com.HaikuMasterTrainingDataProcessor.word2vec.util.StopWordsCache;
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

    private MorphologicalProcessor morphologicalProcessor;

    public PosTaggerImpl() {
        props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos");
        pipeline = new StanfordCoreNLP(props);
        morphologicalProcessor = new MorphologicalProcessorImpl();
    }

    @Override
    public SentenceData tag(String inputSentence) {
        SentenceData sentenceData = new SentenceData();
        StringBuilder sentence = new StringBuilder();
        sentence.append("");
        List<TokenTagData> tokenTagDataList = new ArrayList<>();
        Annotation annotation = new Annotation(inputSentence);
        pipeline.annotate(annotation);
        if (annotation.get(CoreAnnotations.SentencesAnnotation.class).size() > 0) {
            CoreMap processedSentence = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
            for (CoreLabel token : processedSentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String tag = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                if (TagsCache.tags.contains(tag) && !StopWordsCache.stopWordsCache.contains(word)) {
                    if (TagsCache.verbTags.contains(tag) && ((word.endsWith("ing") || word.endsWith("ed")))) {
                        word = morphologicalProcessor.removeSuffix(word);
                    }
                    TokenTagData tokenTagData = new TokenTagData(word, TagsCache.nounTags.contains(tag), TagsCache.adjectiveTags.contains(tag),
                            TagsCache.verbTags.contains(tag), TagsCache.adverbTags.contains(tag), TagsCache.tagsConversionMap.get(tag));
                    tokenTagDataList.add(tokenTagData);
                    sentence.append(word);
                    sentence.append(" ");
                }
            }
        }
        sentenceData.setTokenTagDataList(tokenTagDataList);
        sentenceData.setTaggedSentence(sentence.toString());
        return sentenceData;
    }

}