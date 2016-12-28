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
 * @see {@link #forSearch()}
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
     * @return {@link Word2VecSearcher} for searching
     */
    public Word2VecSearcher forSearch() {
        return new Word2VecSearcherImpl(this);
    }


    /**
     * Forwards to {@link #fromBinFile(File, ByteOrder, ProfilingTimer)} with the default
     * ByteOrder.LITTLE_ENDIAN and no ProfilingTimer
     */
    public static Word2VecModel fromBinFile(File file)
            throws IOException {
        return fromBinFile(file, ByteOrder.LITTLE_ENDIAN, ProfilingTimer.NONE);
    }

    /**
     * Forwards to {@link #fromBinFile(File, ByteOrder, ProfilingTimer)} with no ProfilingTimer
     */
    public static Word2VecModel fromBinFile(File file, ByteOrder byteOrder)
            throws IOException {
        return fromBinFile(file, byteOrder, ProfilingTimer.NONE);
    }

    /**
     * @return {@link Word2VecModel} created from the binary representation output
     * by the open source C version of word2vec using the given byte order.
     */
    public static Word2VecModel fromBinFile(File file, ByteOrder byteOrder, ProfilingTimer timer)
            throws IOException {

        try (
                final FileInputStream fis = new FileInputStream(file);
                final AC ac = timer.start("Loading vectors from bin file")
        ) {
            final FileChannel channel = fis.getChannel();
            timer.start("Reading gigabyte #1");
            MappedByteBuffer buffer =
                    channel.map(
                            FileChannel.MapMode.READ_ONLY,
                            0,
                            Math.min(channel.size(), Integer.MAX_VALUE));
            buffer.order(byteOrder);
            int bufferCount = 1;
            // Java's NIO only allows memory-mapping up to 2GB. To work around this problem, we re-map
            // every gigabyte. To calculate offsets correctly, we have to keep track how many gigabytes
            // we've already skipped. That's what this is for.

            StringBuilder sb = new StringBuilder();
            char c = (char) buffer.get();
            while (c != '\n') {
                sb.append(c);
                c = (char) buffer.get();
            }
            String firstLine = sb.toString();
            int index = firstLine.indexOf(' ');
            Preconditions.checkState(index != -1,
                    "Expected a space in the first line of file '%s': '%s'",
                    file.getAbsolutePath(), firstLine);

            final int vocabSize = Integer.parseInt(firstLine.substring(0, index));
            final int layerSize = Integer.parseInt(firstLine.substring(index + 1));
            timer.appendToLog(String.format(
                    "Loading %d vectors with dimensionality %d",
                    vocabSize,
                    layerSize));

            List<String> vocabs = new ArrayList<String>(vocabSize);
            DoubleBuffer vectors = ByteBuffer.allocateDirect(vocabSize * layerSize * 8).asDoubleBuffer();

            long lastLogMessage = System.currentTimeMillis();
            final float[] floats = new float[layerSize];
            for (int lineno = 0; lineno < vocabSize; lineno++) {
                // read vocab
                sb.setLength(0);
                c = (char) buffer.get();
                while (c != ' ') {
                    // ignore newlines in front of words (some binary files have newline,
                    // some don't)
                    if (c != '\n') {
                        sb.append(c);
                    }
                    c = (char) buffer.get();
                }
                vocabs.add(sb.toString());

                // read vector
                final FloatBuffer floatBuffer = buffer.asFloatBuffer();
                floatBuffer.get(floats);
                for (int i = 0; i < floats.length; ++i) {
                    vectors.put(lineno * layerSize + i, floats[i]);
                }
                buffer.position(buffer.position() + 4 * layerSize);

                // print log
                final long now = System.currentTimeMillis();
                if (now - lastLogMessage > 1000) {
                    final double percentage = ((double) (lineno + 1) / (double) vocabSize) * 100.0;
                    timer.appendToLog(
                            String.format("Loaded %d/%d vectors (%f%%)", lineno + 1, vocabSize, percentage));
                    lastLogMessage = now;
                }

                // remap file
                if (buffer.position() > ONE_GB) {
                    final int newPosition = (int) (buffer.position() - ONE_GB);
                    final long size = Math.min(channel.size() - ONE_GB * bufferCount, Integer.MAX_VALUE);
                    timer.endAndStart(
                            "Reading gigabyte #%d. Start: %d, size: %d",
                            bufferCount,
                            ONE_GB * bufferCount,
                            size);
                    buffer = channel.map(
                            FileChannel.MapMode.READ_ONLY,
                            ONE_GB * bufferCount,
                            size);
                    buffer.order(byteOrder);
                    buffer.position(newPosition);
                    bufferCount += 1;
                }
            }
            timer.end();

            return new Word2VecModel(vocabs, layerSize, vectors);
        }
    }

    /**
     * Saves the model as a bin file that's compatible with the C version of Word2Vec
     */
    public void toBinFile(final OutputStream out) throws IOException {
        final Charset cs = Charset.forName("UTF-8");
        final String header = String.format("%d %d\n", vocab.size(), layerSize);
        out.write(header.getBytes(cs));

        final double[] vector = new double[layerSize];
        final ByteBuffer buffer = ByteBuffer.allocate(4 * layerSize);
        buffer.order(ByteOrder.LITTLE_ENDIAN);    // The C version uses this byte order.
        for (int i = 0; i < vocab.size(); ++i) {
            out.write(String.format("%s ", vocab.get(i)).getBytes(cs));

            vectors.position(i * layerSize);
            vectors.get(vector);
            buffer.clear();
            for (int j = 0; j < layerSize; ++j)
                buffer.putFloat((float) vector[j]);
            out.write(buffer.array());

            out.write('\n');
        }

        out.flush();
    }

    /**
     * @return {@link Word2VecTrainerBuilder} for training a model
     */
    public static Word2VecTrainerBuilder trainer() {
        return new Word2VecTrainerBuilder();
    }
}
