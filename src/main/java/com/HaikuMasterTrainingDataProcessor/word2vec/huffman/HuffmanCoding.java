package com.HaikuMasterTrainingDataProcessor.word2vec.huffman;

import com.HaikuMasterTrainingDataProcessor.word2vec.data.HuffmanNode;
import com.HaikuMasterTrainingDataProcessor.word2vec.training.Word2VecTrainerBuilder;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset.Entry;

import java.util.ArrayList;
import java.util.Map;

/**
 * Word2Vec library relies on a Huffman encoding scheme
 * <p>
 * Note that the generated codes and the index of the parents are both used in the
 * hierarchical softmax portions of the neural network training phase
 * <p>
 *     https://www.siggraph.org/education/materials/HyperGraph/video/mpeg/mpegfaq/huffman_tutorial.html
 *
 *     Lets say you have a set of numbers and their frequency of use and want to create a huffman encoding for them:
 FREQUENCY	VALUE
 ---------       -----
 5            1
 7            2
 10            3
 15            4
 20            5
 45            6
 Creating a huffman tree is simple. Sort this list by frequency and make the two-lowest elements into leaves, creating a parent node with a frequency that is the sum of the two lower element's frequencies:
 12:*
 /       \
 5:1            7:2
 The two elements are removed from the list and the new parent node, with frequency 12, is inserted into the list by frequency. So now the list, sorted by frequency, is:
 10:3
 12:*
 15:4
 20:5
 45:6
 You then repeat the loop, combining the two lowest elements. This results in:
 22:*
 /     \
 10:3       12:*
 /   \
 5:1   7:2
 and the list is now:
 15:4
 20:5
 22:*
 45:6
 You repeat until there is only one element left in the list.
 35:*
 /   \
 15:4  20:5

 22:*
 35:*
 45:6

 57:*
 ___/    \___
 /            \
 22:*          35:*
 /   \          /   \
 10:3   12:*     15:4   20:5
 /   \
 5:1   7:2

 45:6
 57:*

 102:*
 __________________/    \__
 /                          \
 57:*                         45:6
 ___/    \___
 /            \
 22:*          35:*
 /   \          /   \
 10:3   12:*     15:4   20:5
 /   \
 5:1   7:2
 Now the list is just one element containing 102:*, you are done.
 This element becomes the root of your binary huffman tree. To generate a huffman code you traverse the tree to the value you want,
 outputing a 0 every time you take a lefthand branch, and a 1 every time you take a righthand branch.
 (normally you traverse the tree backwards from the code you want and build the binary huffman encoding string backwards as well, since the first bit must start from the top).

 Example: The encoding for the value 4 (15:4) is 010. The encoding for the value 6 (45:6) is 1

 Decoding a huffman encoding is just as easy : as you read bits in from your input stream you traverse the tree beginning at the root, taking the left hand path if you read a 0 and the right hand path if you read a 1. When you hit a leaf, you have found the code.

 Generally, any huffman compression scheme also requires the huffman tree to be written out as part of the file, otherwise the reader cannot decode the data.
 For a static tree, you don't have to do this since the tree is known and fixed.

 The easiest way to output the huffman tree itself is to, starting at the root, dump first the left hand side then the right hand side.
 For each node you output a 0, for each leaf you output a 1 followed by N bits representing the value.
 For example, the partial tree in my last example above using 4 bits per value can be represented as follows:

 000100 fixed 6 bit byte indicates how many bits the value
 for each leaf is stored in.  In this case, 4.
 0      root is a node
 left hand side is
 10011  a leaf with value 3
 right hand side is
 0      another node
 recurse down, left hand side is
 10001  a leaf with value 1
 right hand side is
 10010  a leaf with value 2
 recursion return
 So the partial tree can be represented with 00010001001101000110010, or 23 bits. Not bad!
 */
public class HuffmanCoding {

	private final ImmutableMultiset<String> vocab;
	private final Word2VecTrainerBuilder.TrainingProgressListener listener;
	
	/**
     * @param vocab  Multiset of tokens, sorted by frequency descending
     * @param listener Progress listener
	 */
	public HuffmanCoding(ImmutableMultiset<String> vocab, Word2VecTrainerBuilder.TrainingProgressListener listener) {
		this.vocab = vocab;
		this.listener = listener;
	}
	
	/**
	 * @return {@link Map} from each given token to a {@link HuffmanNode} 
	 */
	public Map<String, HuffmanNode> encode() throws InterruptedException {
		final int numTokens = vocab.elementSet().size();
		
		int[] parentNode = new int[numTokens * 2 + 1];
		byte[] binary = new byte[numTokens * 2 + 1];
		long[] count = new long[numTokens * 2 + 1];
		int i = 0;
		for (Entry<String> e : vocab.entrySet()) {
			count[i] = e.getCount();
			i++;
		}
		Preconditions.checkState(i == numTokens, "Expected %s to match %s", i, numTokens);
		for (i = numTokens; i < count.length; i++)
			count[i] = (long)1e15;
		
		createTree(numTokens, count, binary, parentNode);
		
		return encode(binary, parentNode);
	}
	
	/** 
	 * Populate the count, binary, and parentNode arrays with the Huffman tree
     * This uses the linear time method assuming that the count array is sorted
     * EXPLANATION HERE: http://www.trevorsimonton.com/blog/2016/12/15/huffman-tree-in-word2vec.html
     *
     * EXAMPLE SENTENCE: how much wood would a wood chuck chuck if a wood chuck could chuck wood
     * Result tree for above sentence:
     * Vocab size: 9
     Words in train file: 16
     </s>:
     count: 1
     point: arr
     code: 10
     codelen: 2

     wood:
     count: 4
     point: arr
     code: 01
     codelen: 2

     chuck:
     count: 4
     point: arr
     code: 00
     codelen: 2

     a:
     count: 2
     point: arr
     code: 1111
     codelen: 4

     how:
     count: 1
     point: arr
     code: 1100
     codelen: 4

     much:
     count: 1
     point: arr
     code: 11101
     codelen: 5

     would:
     count: 1
     point: arr
     code: 11100
     codelen: 5

     if:
     count: 1
     point: arr
     code: 11011
     codelen: 5

     could:
     count: 1
     point: arr
     code: 11010
     codelen: 5

     builds a hash table of word structs. Using this hash table, the function counts repeat occurrences of unique words.

     The “learn” function also builds vocab, which gives each unique word in the vocabulary an index in an array.

     In this example, after the “learn” function runs, the vocab array looks like this:

     0: </s>
     1: wood
     2: chuck
     3: a
     4: how
     5: much
     6: would
     7: if
     8: could

     The count of each word in vocab is passed to the count array, and then we initialize up to vocab_size * 2 slots in this array to a maximum value.
     These extra slots are the parent, non-leaf nodes in our Huffman tree.


     */
    private void createTree(int numTokens, long[] count, byte[] binary, int[] parentNode) throws InterruptedException {
		int min1i;
		int min2i;
		int pos1 = numTokens - 1;
		int pos2 = numTokens;
		
		// Construct the Huffman tree by adding one node at a time
		for (int a = 0; a < numTokens - 1; a++) {
			// First, find two smallest nodes 'min1, min2'
			if (pos1 >= 0) { 
				if (count[pos1] < count[pos2]) {
					min1i = pos1;
					pos1--;
				} else {
					min1i = pos2;
					pos2++;
				}
			} else {
				min1i = pos2;
				pos2++;
			}
			
			if (pos1 >= 0) {
				if (count[pos1] < count[pos2]) {
					min2i = pos1;
					pos1--;
				} else {
					min2i = pos2;
					pos2++;
				}
			} else {
				min2i = pos2;
				pos2++;
			}
			
			int newNodeIdx = numTokens + a;
			count[newNodeIdx] = count[min1i] + count[min2i];
			parentNode[min1i] = newNodeIdx;
			parentNode[min2i] = newNodeIdx;
			binary[min2i] = 1;
			
			if (a % 1_000 == 0) {
				if (Thread.currentThread().isInterrupted())
					throw new InterruptedException("Interrupted while encoding huffman tree");
				listener.update(Word2VecTrainerBuilder.TrainingProgressListener.Stage.CREATE_HUFFMAN_ENCODING, (0.5 * a) / numTokens);
			}
		}
	}
	
	/** @return Ordered map from each token to its {@link HuffmanNode}, ordered by frequency descending */
	private Map<String, HuffmanNode> encode(byte[] binary, int[] parentNode) throws InterruptedException {
		int numTokens = vocab.elementSet().size();
		
		// Now assign binary code to each unique token
		ImmutableMap.Builder<String, HuffmanNode> result = ImmutableMap.builder();
		int nodeIdx = 0;
		for (Entry<String> e : vocab.entrySet()) {
			int curNodeIdx = nodeIdx;
			ArrayList<Byte> code = new ArrayList<>();
			ArrayList<Integer> points = new ArrayList<>();
			while (true) {
				code.add(binary[curNodeIdx]);
				points.add(curNodeIdx);
				curNodeIdx = parentNode[curNodeIdx];
				if (curNodeIdx == numTokens * 2 - 2)
					break;
			}
			int codeLen = code.size();
			final int count = e.getCount();
			final byte[] rawCode = new byte[codeLen];
			final int[] rawPoints = new int[codeLen + 1];
			
			rawPoints[0] = numTokens - 2;
			for (int i = 0; i < codeLen; i++) {
				rawCode[codeLen - i - 1] = code.get(i);
				rawPoints[codeLen - i] = points.get(i) - numTokens;
			}
			
			String token = e.getElement();
			result.put(token, new HuffmanNode(rawCode, rawPoints, nodeIdx, count));
			
			if (nodeIdx % 1_000 == 0) {
				if (Thread.currentThread().isInterrupted())
					throw new InterruptedException("Interrupted while encoding huffman tree");
				listener.update(Word2VecTrainerBuilder.TrainingProgressListener.Stage.CREATE_HUFFMAN_ENCODING, 0.5 + (0.5 * nodeIdx) / numTokens);
			}
			
			nodeIdx++;
		}
		
		return result.build();
	}
}
