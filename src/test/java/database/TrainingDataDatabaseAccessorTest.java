package database;

import com.HaikuMasterTrainingDataProcessor.database.TrainingDataDatabaseAccessor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Oliver on 2/9/2017.
 */
public class TrainingDataDatabaseAccessorTest {

    private TrainingDataDatabaseAccessor trainingDataDatabaseAccessor;

    @Before
    public void setup() {
        final ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        trainingDataDatabaseAccessor = (TrainingDataDatabaseAccessor) context.getBean("trainingDataDatabaseAccessor");
    }

    @Test
    public void testUpdateNumberOfSentences() {
        trainingDataDatabaseAccessor.insertNumberOfSentences(120834);
    }
}
