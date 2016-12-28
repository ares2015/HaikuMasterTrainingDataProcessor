package com.HaikuMasterTrainingDataProcessor.word2vec.model;


import java.nio.DoubleBuffer;

/**
 * Represents a word2vec model where all the vectors are normalized to unit length.
 */
public class NormalizedWord2VecModel extends Word2VecModel {

	private NormalizedWord2VecModel(Iterable<String> vocab, int layerSize, final DoubleBuffer vectors) {
		super(vocab, layerSize, vectors);
		normalize();
	}


	public static NormalizedWord2VecModel fromWord2VecModel(Word2VecModel model) {
		return new NormalizedWord2VecModel(model.vocab, model.layerSize, model.vectors.duplicate());
	}

	/** Normalizes the vectors in this model */
	private void normalize() {
		for(int i = 0; i < vocab.size(); ++i) {
			double len = 0;
			for(int j = i * layerSize; j < (i + 1) * layerSize; ++j)
				len += vectors.get(j) * vectors.get(j);
			len = Math.sqrt(len);

			for(int j = i * layerSize; j < (i + 1) * layerSize; ++j)
				vectors.put(j, vectors.get(j) / len);
		}
	}
}
