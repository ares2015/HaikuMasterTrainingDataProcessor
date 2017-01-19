package com.HaikuMasterTrainingDataProcessor.database;

import com.HaikuMasterTrainingDataProcessor.tagging.data.TokenTagData;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Oliver on 12/17/2016.
 */
public class TrainingDataDatabaseAccessorImpl implements TrainingDataDatabaseAccessor {

    private JdbcTemplate jdbcTemplate;

    public TrainingDataDatabaseAccessorImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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