package com.HaikuMasterTrainingDataProcessor.tagging.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Oliver on 12/17/2016.
 */
public final class TagsCache {

     /* Stanford tags
    1.	CC	Coordinating conjunction
2.	CD	Cardinal number
3.	DT	Determiner
4.	EX	Existential there
5.	FW	Foreign word
6.	IN	Preposition or subordinating conjunction
7.	JJ	Adjective
8.	JJR	Adjective, comparative
9.	JJS	Adjective, superlative
10.	LS	List item marker
11.	MD	Modal
12.	NN	Noun, singular or mass
13.	NNS	Noun, plural
14.	NNP	Proper noun, singular
15.	NNPS	Proper noun, plural
16.	PDT	Predeterminer
17.	POS	Possessive ending
18.	PRP	Personal pronoun
19.	PRP$	Possessive pronoun
20.	RB	Adverb
21.	RBR	Adverb, comparative
22.	RBS	Adverb, superlative
23.	RP	Particle
24.	SYM	Symbol
25.	TO	to
26.	UH	Interjection
27.	VB	Verb, base form
28.	VBD	Verb, past tense
29.	VBG	Verb, gerund or present participle
30.	VBN	Verb, past participle
31.	VBP	Verb, non-3rd person singular present
32.	VBZ	Verb, 3rd person singular present
33.	WDT	Wh-determiner
34.	WP	Wh-pronoun
35.	WP$	Possessive wh-pronoun
36.	WRB	Wh-adverb
     */

    public static Set<String> tags = new HashSet<>();

    public static Set<String> nounTags = new HashSet<>();

    public static Set<String> adjectiveTags = new HashSet<>();

    public static Set<String> verbTags = new HashSet<>();

    public static Set<String> adverbTags = new HashSet<>();

    public static Map<String, String> tagsConversionMap = new HashMap<String, String>();

    static {
        tags.add("IN");

        tags.add("NN");
        tags.add("NNS");
//        tags.add("NNP");
//        tags.add("NNPS");
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

        tagsConversionMap.put("NN", "N");
        tagsConversionMap.put("NNS", "N");
        tagsConversionMap.put("NNP", "N");
        tagsConversionMap.put("NNPS", "N");

        tagsConversionMap.put("JJ", "AJ");
        tagsConversionMap.put("JJR", "AJ");
        tagsConversionMap.put("JJS", "AJ");

        tagsConversionMap.put("VB", "V");
        tagsConversionMap.put("VBD", "V");
        tagsConversionMap.put("VBG", "V");
        tagsConversionMap.put("VBN", "V");
        tagsConversionMap.put("VBP", "V");
        tagsConversionMap.put("VBZ", "V");

        tagsConversionMap.put("RB", "AV");
        tagsConversionMap.put("RBR", "AV");
        tagsConversionMap.put("RBS", "AV");
    }
}