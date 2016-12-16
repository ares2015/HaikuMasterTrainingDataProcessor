package reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Oliver on 12/16/2016.
 */
public class TextReaderImpl implements TextReader {

    private String filePath;

    public TextReaderImpl(String filePath) {
        this.filePath = filePath;
    }

    public List<String> read() {
        List<String> sentences = null;
        String wholeText = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String testDataRow = br.readLine();
            while (testDataRow != null) {
                if (!"".equals(testDataRow)) {
                    wholeText += testDataRow;
                    testDataRow += " ";
                }
                testDataRow = br.readLine();
            }
            sentences = Arrays.asList(wholeText.split("[\\.\\!\\?]"));

        } catch (final IOException e) {
            e.printStackTrace();

        }
        return sentences;
    }

}
