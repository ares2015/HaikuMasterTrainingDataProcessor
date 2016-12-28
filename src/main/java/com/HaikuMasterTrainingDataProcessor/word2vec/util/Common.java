package com.HaikuMasterTrainingDataProcessor.word2vec.util;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Simple utilities that in no way deserve their own class.
 */
public class Common {
    /**
     * @param distance use 1 for our caller, 2 for their caller, etc...
     * @return the stack trace element from where the calling method was invoked
     */
    public static StackTraceElement myCaller(int distance) {
        // 0 here, 1 our caller, 2 their caller
        int index = distance + 1;
        try {
            StackTraceElement st[] = new Throwable().getStackTrace();
            // hack: skip synthetic caster methods
            if (st[index].getLineNumber() == 1) return st[index + 1];
            return st[index];
        } catch (Throwable t) {
            return new StackTraceElement("[unknown]", "-", "-", 0);
        }
    }

    /**
     * Serialize the given object into the given stream
     */
    public static void serialize(Serializable obj, ByteArrayOutputStream bout) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(obj);
            out.close();
        } catch (IOException e) {
            throw new IllegalStateException("Could not serialize " + obj, e);
        }
    }

    /**
     * Read the file line for line and return the result in a list
     *
     * @throws IOException upon failure in reading, note that we wrap the underlying IOException with the file name
     */
    public static List<String> readToList(File f) throws IOException {
        try (final Reader reader = asReaderUTF8Lenient(new FileInputStream(f))) {
            return readToList(reader);
        } catch (IOException ioe) {
            throw new IllegalStateException(String.format("Failed to read %s: %s", f.getAbsolutePath(), ioe), ioe);
        }
    }

    /**
     * Read the Reader line for line and return the result in a list
     */
    public static List<String> readToList(Reader r) throws IOException {
        try (BufferedReader in = new BufferedReader(r)) {
            List<String> l = new ArrayList<>();
            String line = null;
            while ((line = in.readLine()) != null) {
                String filteredSentence = removeStopWordsFromSentence(line);
                l.add(filteredSentence);
            }
            return Collections.unmodifiableList(l);
        }
    }

    private static String removeStopWordsFromSentence(String sentence) {
        String[] tokTmp;
        tokTmp = sentence.split("\\ ");
        List<String> filteredTokens = new ArrayList<>();
        for (String token : tokTmp) {
            if (!(StopWordsCache.stopWordsCache.contains(token))) {
                filteredTokens.add(token);
            }
        }
        final List<String> tokens = removeEmptyStringInSentence(filteredTokens);
        return convertListToString(tokens);
    }

    private static List<String> removeEmptyStringInSentence(List<String> filteredTokens) {
        final List<String> listTokens = new ArrayList<String>();
        for (final String token : filteredTokens) {
            if (!token.equals("")) {
                listTokens.add(token);
            }
        }
        return listTokens;
    }

    private static String convertListToString(List<String> list) {
        String newString = "";
        int i = 0;
        for (String word : list) {
            if (i < list.size() - 1) {
                newString += word + " ";
            } else {
                newString += word;
            }
            i++;
        }
        return newString;
    }

    /**
     * Wrap the InputStream in a Reader that reads UTF-8. Invalid content will be replaced by unicode replacement glyph.
     */
    public static Reader asReaderUTF8Lenient(InputStream in) {
        return new UnicodeReader(in, "utf-8");
    }

    /**
     * @return true if i is an even number
     */
    public static boolean isEven(int i) {
        return (i & 1) == 0;
    }

    /**
     * @return true if i is an odd number
     */
    public static boolean isOdd(int i) {
        return !isEven(i);
    }


    /**
     * Get an input stream to read the raw contents of the given resource, remember to close it :)
     */
    public static InputStream getResourceAsStream(Class<?> clazz, String fn) throws IOException {
        InputStream stream = clazz.getResourceAsStream(fn);
        if (stream == null) {
            throw new IOException("resource \"" + fn + "\" relative to " + clazz + " not found.");
        }
        return unpackStream(stream, fn);
    }


    /**
     * @throws IOException if {@code is} is null or if an {@link IOException} is thrown when reading from {@code is}
     */
    public static InputStream unpackStream(InputStream is, String fn) throws IOException {
        if (is == null)
            throw new FileNotFoundException("InputStream is null for " + fn);

        switch (FilenameUtils.getExtension(fn).toLowerCase()) {
            case "gz":
                return new GZIPInputStream(is);
            default:
                return is;
        }
    }

}
