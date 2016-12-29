package com.HaikuMasterTrainingDataProcessor.word2vec.model;

import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcher;
import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcherImpl;
import com.HaikuMasterTrainingDataProcessor.word2vec.training.Word2VecTrainerBuilder;
import com.HaikuMasterTrainingDataProcessor.word2vec.util.AC;
import com.HaikuMasterTrainingDataProcessor.word2vec.util.ProfilingTimer;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
}
