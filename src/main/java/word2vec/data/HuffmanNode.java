package word2vec.data;

/**
 * Created by Oliver on 12/16/2016.
 */
public class HuffmanNode {
    /**
     * Array of 0's and 1's
     */
    public final byte[] code;
    /**
     * Array of parent node index offsets
     */
    public final int[] point;
    /**
     * Index of the Huffman node
     */
    public final int idx;
    /**
     * Frequency of the token
     */
    public final int count;

    private HuffmanNode(byte[] code, int[] point, int idx, int count) {
        this.code = code;
        this.point = point;
        this.idx = idx;
        this.count = count;
    }
}
