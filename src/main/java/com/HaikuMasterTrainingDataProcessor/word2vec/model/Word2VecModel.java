package com.HaikuMasterTrainingDataProcessor.word2vec.model;

import com.HaikuMasterTrainingDataProcessor.word2vec.training.Word2VecTrainerBuilder;
import com.google.common.collect.ImmutableList;

import java.nio.DoubleBuffer;
import java.util.List;


/**
 * Represents the Word2Vec model, containing vectors for each word
 * <p/>
 * Instances of this class are obtained via:
 * <ul>
 * <li> {@link #trainer()}
 * </ul>
 *
 */
public class Word2VecModel {
    public final List<String> vocab;
    public final int layerSize;
    public final DoubleBuffer vectors;
    private final static long ONE_GB = 1024 * 1024 * 1024;
    private List<String> sentences;

    Word2VecModel(Iterable<String> vocab, int layerSize, DoubleBuffer vectors) {
        this.vocab = ImmutableList.copyOf(vocab);
        this.layerSize = layerSize;
        this.vectors = vectors;
    }

    public Word2VecModel(Iterable<String> vocab, int layerSize, double[] vectors) {
        this(vocab, layerSize, DoubleBuffer.wrap(vectors));
    }

    /**
     * @return Vocabulary
     */
    public Iterable<String> getVocab() {
        return vocab;
    }

    /**
     * @return {@link Word2VecTrainerBuilder} for training a model
     */
    public static Word2VecTrainerBuilder trainer() {
        return new Word2VecTrainerBuilder();
    }

    public void setSentences(List<String> sentences){
        this.sentences = sentences;
    }

    public List<String> getSentences(){
        return sentences;
    }

}