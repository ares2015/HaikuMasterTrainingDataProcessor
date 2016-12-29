package com.HaikuMasterTrainingDataProcessor.word2vec.util;

import java.io.*;


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

}