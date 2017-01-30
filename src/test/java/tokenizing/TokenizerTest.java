package tokenizing;

import com.HaikuMasterTrainingDataProcessor.tokenizing.Tokenizer;
import com.HaikuMasterTrainingDataProcessor.tokenizing.TokenizerImpl;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by oliver.eder on 12/29/2016.
 */
public class TokenizerTest {

    private Tokenizer tokenizer = new TokenizerImpl();

    @Test
    public void testRemoveSpecialCharacters() {
        String token = ":;.dog,!?'''''_“";
        assertEquals("dog", tokenizer.removeSpecialCharacters(token));
        assertEquals("she", tokenizer.removeSpecialCharacters("_she_"));
    }

    @Test
    public void testDecapitalize(){
        String token = "Dog";
        assertEquals("dog", tokenizer.decapitalize(token));
    }

    @Test
    public void testContainsSpecialChars(){
        String token = ":;.dog,!?'''''";
        assertTrue(tokenizer.containsSpecialChars(token));
    }

    @Test
    public void testIsLowerCase(){
        String token = "Dog";
        assertFalse(tokenizer.isLowerCase(token));
    }


}