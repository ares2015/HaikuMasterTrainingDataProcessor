package writer;

import com.HaikuMasterTrainingDataProcessor.preprocessor.data.BookData;
import com.HaikuMasterTrainingDataProcessor.writer.TrainingDataWriter;
import com.HaikuMasterTrainingDataProcessor.writer.TrainingDataWriterImpl;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2/6/2017.
 */
public class TrainingDataWriterTest {

    @Test
    public void test() throws IOException {
        TrainingDataWriter trainingDataWriter = new TrainingDataWriterImpl();
        List<String> sentences = new ArrayList<String>();
        sentences.add("test");
        BookData bookData = new BookData("Test", sentences);
        trainingDataWriter.writePreprocessedDataIntoBooksDirectory(bookData);
    }
}
