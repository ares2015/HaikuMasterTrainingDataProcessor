package tokens;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oliver on 12/16/2016.
 */
public class TokenizerTest {

    Tokenizer tokenizer = new TokenizerImpl();

    @Test
    public void testRemoveTextReferenceBrackets() {
        String testString = "[22][23]abc def";
        assertEquals("abc def", tokenizer.removeBrackets(testString, '[', ']'));
    }

    @Test
    public void testRemoveNestedSentenceBrackets() {
        String testString = "ab (c) d";
        assertEquals("ab  d", tokenizer.removeBrackets(testString, '(', ')'));
    }

    @Test
    public void testRemoveDoubleQuotes() {
        System.out.println("\"king's ships\"");
        assertEquals("king's ships", tokenizer.removeDoubleQuotes("\"king's ships\""));
        System.out.println(tokenizer.removeDoubleQuotes("\"king's ships\""));
    }

    @Test
    public void testRemoveEmptyStrings() {
        assertEquals("a bc d e", tokenizer.removeEmptyStrings("a   bc  d e"));
    }
}
