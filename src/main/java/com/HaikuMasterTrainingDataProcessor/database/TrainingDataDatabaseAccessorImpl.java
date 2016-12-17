package com.HaikuMasterTrainingDataProcessor.database;

import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;
import com.HaikuMasterTrainingDataProcessor.word2vec.search.Word2VecSearcher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by Oliver on 12/17/2016.
 */
public class TrainingDataDatabaseAccessorImpl implements TrainingDataDatabaseAccessor {

    private JdbcTemplate jdbcTemplate;

    public TrainingDataDatabaseAccessorImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertTokenWord2VecData(String token, List<Word2VecSearcher.Match> matches) {
        final String sql = "insert into jos_haiku_master_word2vec_model (token, " +
                "neighbour1, neighbour1_value," +
                "neighbour2, neighbour2_value," +
                "neighbour3, neighbour3_value," +
                "neighbour4, neighbour4_value," +
                "neighbour5, neighbour5_value," +
                "neighbour6, neighbour6_value," +
                "neighbour7, neighbour7_value," +
                "neighbour8, neighbour8_value," +
                "neighbour9, neighbour9_value," +
                "neighbour10, neighbour10_value)" +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, new Object[]{token,
                matches.get(0).match(), matches.get(0).distance(),
                matches.get(1).match(), matches.get(1).distance(),
                matches.get(2).match(), matches.get(2).distance(),
                matches.get(3).match(), matches.get(3).distance(),
                matches.get(4).match(), matches.get(4).distance(),
                matches.get(5).match(), matches.get(5).distance(),
                matches.get(6).match(), matches.get(6).distance(),
                matches.get(7).match(), matches.get(7).distance(),
                matches.get(8).match(), matches.get(8).distance(),
                matches.get(9).match(), matches.get(9).distance(),
        });
    }

    @Override
    public void insertTokenTagData(TokenTagData tokenTagData) {
        String sql = "";
        String token = tokenTagData.getToken();
        if (tokenExistsInDatabase(token)) {
            if (tokenTagData.isNoun()) {
                int isNoun = tokenTagData.isNoun() ? 1 : 0;
                sql = "update jos_haiku_master_token_tag_data set is_noun = ? where token = ?";
                jdbcTemplate.update(sql, new Object[]{isNoun, token});
            } else if (tokenTagData.isAdjective()) {
                int isAdjective = tokenTagData.isAdjective() ? 1 : 0;
                sql = "update jos_haiku_master_token_tag_data set is_adjective = ? where token = ?";
                jdbcTemplate.update(sql, new Object[]{isAdjective, token});
            } else if (tokenTagData.isVerb()) {
                int isVerb = tokenTagData.isAdjective() ? 1 : 0;
                sql = "update jos_haiku_master_token_tag_data set is_verb = ? where token = ?";
                jdbcTemplate.update(sql, new Object[]{isVerb, token});
            } else if (tokenTagData.isAdverb()) {
                int isAdverb = tokenTagData.isAdverb() ? 1 : 0;
                sql = "update jos_haiku_master_token_tag_data set is_adverb = ? where token = ?";
                jdbcTemplate.update(sql, new Object[]{isAdverb, token});
            }
        } else {
            sql = "insert into jos_haiku_master_token_tag_data (token, is_noun, is_adjective, is_verb, is_adverb) " +
                    "values (?,?,?,?,?)";
            int isNoun = tokenTagData.isNoun() ? 1 : 0;
            int isAdjective = tokenTagData.isAdjective() ? 1 : 0;
            int isVerb = tokenTagData.isVerb() ? 1 : 0;
            int isAdverb = tokenTagData.isAdverb() ? 1 : 0;
            jdbcTemplate.update(sql, new Object[]{token, isNoun, isAdjective, isVerb, isAdverb
            });
        }
    }

    private boolean tokenExistsInDatabase(String token) {
        int id = -1;
        final String sql = "select id from jos_haiku_master_token_tag_data where token=?";
        try {
            id = jdbcTemplate.queryForInt(sql, new Object[]{token});
        } catch (final EmptyResultDataAccessException e) {
            return false;
        }
        return id > 0;
    }
}