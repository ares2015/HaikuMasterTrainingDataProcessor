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
        String sql = "";
        int vectorId = getVectorId(token);
        if (vectorId > 0) {
            sql = "update jos_haiku_master_word2vec_model set " +
                    "neighbour1 = ?," +
                    "neighbour2 = ?," +
                    "neighbour3 = ?, " +
                    "neighbour4 = ?," +
                    "neighbour5 = ?," +
                    "neighbour6 = ?," +
                    "neighbour7 = ?," +
                    "neighbour8 = ?," +
                    "neighbour9 = ?," +
                    "neighbour10 = ? where id = ?";
            jdbcTemplate.update(sql, new Object[]{
                    matches.get(1).match(),
                    matches.get(2).match(),
                    matches.get(3).match(),
                    matches.get(4).match(),
                    matches.get(5).match(),
                    matches.get(6).match(),
                    matches.get(7).match(),
                    matches.get(8).match(),
                    matches.get(9).match(),
                    matches.get(10).match(), vectorId});
        } else {
            sql = "insert into jos_haiku_master_word2vec_model (token, " +
                    "neighbour1," +
                    "neighbour2," +
                    "neighbour3," +
                    "neighbour4," +
                    "neighbour5," +
                    "neighbour6," +
                    "neighbour7," +
                    "neighbour8," +
                    "neighbour9," +
                    "neighbour10) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql, new Object[]{token,
                    matches.get(1).match(),
                    matches.get(2).match(),
                    matches.get(3).match(),
                    matches.get(4).match(),
                    matches.get(5).match(),
                    matches.get(6).match(),
                    matches.get(7).match(),
                    matches.get(8).match(),
                    matches.get(9).match(),
                    matches.get(10).match()});
        }
    }

    @Override
    public void insertTokenTagData(TokenTagData tokenTagData) {
        String sql = "";
        String token = tokenTagData.getToken();
        int tokenId = getTokenId(token);
        if (tokenId > 0) {
            if (tokenTagData.isNoun()) {
                int isNoun = tokenTagData.isNoun() ? 1 : 0;
                sql = "update jos_haiku_master_token_tag_data set is_noun = ? where id = ?";
                jdbcTemplate.update(sql, new Object[]{isNoun, tokenId});
            } else if (tokenTagData.isAdjective()) {
                int isAdjective = tokenTagData.isAdjective() ? 1 : 0;
                sql = "update jos_haiku_master_token_tag_data set is_adjective = ? where id = ?";
                jdbcTemplate.update(sql, new Object[]{isAdjective, tokenId});
            } else if (tokenTagData.isVerb()) {
                int isVerb = tokenTagData.isAdjective() ? 1 : 0;
                sql = "update jos_haiku_master_token_tag_data set is_verb = ? where id = ?";
                jdbcTemplate.update(sql, new Object[]{isVerb, tokenId});
            } else if (tokenTagData.isAdverb()) {
                int isAdverb = tokenTagData.isAdverb() ? 1 : 0;
                sql = "update jos_haiku_master_token_tag_data set is_adverb = ? where id = ?";
                jdbcTemplate.update(sql, new Object[]{isAdverb, tokenId});
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

    private int getVectorId(String token) {
        int id = -1;
        final String sql = "select id from jos_haiku_master_word2vec_model where token=?";
        try {
            id = jdbcTemplate.queryForInt(sql, new Object[]{token});
        } catch (final EmptyResultDataAccessException e) {
            return -1;
        }
        return id;
    }


    private int getTokenId(String token) {
        int id = -1;
        final String sql = "select id from jos_haiku_master_token_tag_data where token=?";
        try {
            id = jdbcTemplate.queryForInt(sql, new Object[]{token});
        } catch (final EmptyResultDataAccessException e) {
            return -1;
        }
        return id;
    }
}