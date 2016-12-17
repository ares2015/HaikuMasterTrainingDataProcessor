package com.HaikuMasterTrainingDataProcessor.tagging.cache;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oliver on 12/17/2016.
 */
public final class TagsCache {

    public static Set<String> tags = new HashSet<>();

    static {
        tags.add("NN");
        tags.add("NNS");
        tags.add("NNP");
        tags.add("NNPS");
        tags.add("JJ");
        tags.add("JJR");
        tags.add("JJS");
        tags.add("RB");
        tags.add("RBR");
        tags.add("RBS");
        tags.add("VB");
        tags.add("VBD");
        tags.add("VBG");
        tags.add("VBN");
        tags.add("VBP");
        tags.add("VBZ");
    }
}