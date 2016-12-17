package com.HaikuMasterTrainingDataProcessor.tagging.cache;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oliver on 12/17/2016.
 */
public final class TagsCache {

    public static Set<String> tags = new HashSet<>();

    public static Set<String> nounTags = new HashSet<>();

    public static Set<String> adjectiveTags = new HashSet<>();

    public static Set<String> verbTags = new HashSet<>();

    public static Set<String> adverbTags = new HashSet<>();

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

        nounTags.add("NN");
        nounTags.add("NNS");
        nounTags.add("NNP");
        nounTags.add("NNPS");

        adjectiveTags.add("JJ");
        adjectiveTags.add("JJR");
        adjectiveTags.add("JJS");

        verbTags.add("VB");
        verbTags.add("VBD");
        verbTags.add("VBG");
        verbTags.add("VBN");
        verbTags.add("VBP");
        verbTags.add("VBZ");

        adverbTags.add("RB");
        adverbTags.add("RBR");
        adverbTags.add("RBS");
    }
}