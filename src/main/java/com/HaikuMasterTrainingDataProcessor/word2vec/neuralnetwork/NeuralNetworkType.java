package com.HaikuMasterTrainingDataProcessor.word2vec.neuralnetwork;

import com.HaikuMasterTrainingDataProcessor.word2vec.data.HuffmanNode;
import com.HaikuMasterTrainingDataProcessor.word2vec.training.Word2VecTrainerBuilder;
import com.google.common.collect.Multiset;

import java.util.Map;

/** 
 * Supported types for the neural network
 */
public enum NeuralNetworkType {
	/** Faster, slightly better accuracy for frequent words */
	CBOW {
		@Override NeuralNetworkTrainer createTrainer(NeuralNetworkConfig config, Multiset<String> counts, Map<String, HuffmanNode> huffmanNodes, Word2VecTrainerBuilder.TrainingProgressListener listener) {
			return new CBOWModelTrainer(config, counts, huffmanNodes, listener);
		}
		
		@Override public double getDefaultInitialLearningRate() {
			return 0.05;
		}
	},
	/** Slower, better for infrequent words */
	SKIP_GRAM {
		@Override NeuralNetworkTrainer createTrainer(NeuralNetworkConfig config, Multiset<String> counts, Map<String, HuffmanNode> huffmanNodes, Word2VecTrainerBuilder.TrainingProgressListener listener) {
			return new SkipGramModelTrainer(config, counts, huffmanNodes, listener);
		}
		
		@Override public double getDefaultInitialLearningRate() {
			return 0.025;
		}
	},
	;
	
	/** @return Default initial learning rate */
	public abstract double getDefaultInitialLearningRate();
	
	/** @return New {@link NeuralNetworkTrainer} */
	abstract NeuralNetworkTrainer createTrainer(NeuralNetworkConfig config, Multiset<String> counts, Map<String, HuffmanNode> huffmanNodes, Word2VecTrainerBuilder.TrainingProgressListener listener);
}