package com.HaikuMasterTrainingDataProcessor.database;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Oliver on 2/9/2017.
 */
public class TrainingDataDatabaseAccessorImpl implements TrainingDataDatabaseAccessor {

    private JdbcTemplate jdbcTemplate;

    public TrainingDataDatabaseAccessorImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertNumberOfSentences(int numberOfSentences) {
        String sql = "update jos_haiku_master_number_of_sentences set number = ? where id = 1";
        jdbcTemplate.update(sql, new Object[]{numberOfSentences});
    }

}
