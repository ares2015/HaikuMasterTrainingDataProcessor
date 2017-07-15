package com.HaikuMasterTrainingDataProcessor.tokenizing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oliver.eder on 12/29/2016.
 */
public class TokenizerImpl implements Tokenizer {

    @Override
    public List<String> getTokens(String sentence) {
        String[] tokTmp;
        tokTmp = sentence.split("\\ ");
        final List<String> tokens = removeEmptyStringInSentence(tokTmp);
        List<String> filteredTokens = new ArrayList<>();
        for (String token : tokens) {
            token = removeEmptyCharsFromToken(token);
            filteredTokens.add(token);
        }
        return filteredTokens;
    }

    @Override
    public boolean containsSpecialChars(String token) {
        return token.contains(".") || token.contains(",") || token.contains("!") || token.contains("?")
                || token.contains(":") || token.contains(";")
                || token.contains("\"") || token.contains("\'");
    }

    @Override
    public boolean isLowerCase(String token) {
        return String.valueOf(token.charAt(0)).toLowerCase().equals(String.valueOf(token.charAt(0)));
    }

    @Override
    public String removeSpecialCharacters(String token) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = token.toCharArray();
        for (char c : chars) {
            if ((",".equals(String.valueOf(c)) || "'".equals(String.valueOf(c)) || "’".equals(String.valueOf(c)))
                    || ((Character.isDigit(c) || Character.isLetter(c)))) {
                stringBuilder.append(String.valueOf(c));
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public String decapitalize(String token) {
        char c[] = token.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    /**
     * Removes empty String that is located in sentences with index > 0
     * that is created by empty space behind each sentence (space between
     * end of the sentence and the start of the new sentence). It would be more
     * efficient to remove only first token (empty string) but this loop guarantees
     * that all accidental empty strings are removed.
     *
     * @param tokens Array of tokens with empty strings.
     * @return List of tokens without empty strings.
     */
    private List<String> removeEmptyStringInSentence(final String[] tokens) {
        final List<String> listTokens = new ArrayList<String>();
        for (final String token : tokens) {
            if (!token.equals("")) {
                listTokens.add(token);
            }
        }
        return listTokens;
    }

    private String removeEmptyCharsFromToken(String token) {
        char[] charTmp;
        charTmp = token.toCharArray();
        int numberOfEmptyChars = 0;
        for (int i = 0; i <= charTmp.length - 1; i++) {
            if (charTmp[i] == '\uFEFF') {
                numberOfEmptyChars++;
            }
        }
        return new String(Arrays.copyOfRange(charTmp, numberOfEmptyChars, charTmp.length));
    }
}