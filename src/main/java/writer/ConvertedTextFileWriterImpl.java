package writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Oliver on 12/16/2016.
 */
public class ConvertedTextFileWriterImpl implements ConvertedTextFileWriter {

    private String filePath;

    public ConvertedTextFileWriterImpl(String filePath) {
        this.filePath = filePath;
    }

    public void write(List<String> sentences) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(filePath, true);
            bw = new BufferedWriter(fw);
            for (String sentence : sentences) {
                bw.write(sentence);
                bw.newLine();
            }
            System.out.println("Writing into file finished");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
