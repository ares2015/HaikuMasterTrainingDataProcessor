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
        double distance = matches.get(0).distance();
        double distance1 = matches.get(1).distance();
        double distance2 = matches.get(2).distance();
        double distance3 = matches.get(3).distance();
        double distance4 = matches.get(4).distance();
        double distance5 = matches.get(5).distance();
        double distance6 = matches.get(6).distance();
        double distance7 = matches.get(7).distance();
        double distance8 = matches.get(8).distance();
        double distance9 = matches.get(9).distance();
        int convertedDistance = convertDoubleToInt(100, distance);
        int convertedDistance1 = convertDoubleToInt(100, distance1);
        int convertedDistance2 = convertDoubleToInt(100, distance2);
        int convertedDistance3 = convertDoubleToInt(100, distance3);
        int convertedDistance4 = convertDoubleToInt(100, distance4);
        int convertedDistance5 = convertDoubleToInt(100, distance5);
        int convertedDistance6 = convertDoubleToInt(100, distance6);
        int convertedDistance7 = convertDoubleToInt(100, distance7);
        int convertedDistance8 = convertDoubleToInt(100, distance8);
        int convertedDistance9 = convertDoubleToInt(100, distance9);
        if (vectorId > 0) {
            sql = "update jos_haiku_master_word2vec_model set " +
                    "neighbour1 = ? , neighbour1_value = ?," +
                    "neighbour2 = ?, neighbour2_value = ?," +
                    "neighbour3 = ?, neighbour3_value = ?," +
                    "neighbour4 = ?, neighbour4_value = ?," +
                    "neighbour5 = ?, neighbour5_value = ?," +
                    "neighbour6 = ?, neighbour6_value = ?," +
                    "neighbour7 = ?, neighbour7_value = ?," +
                    "neighbour8 = ?, neighbour8_value = ?," +
                    "neighbour9 = ?, neighbour9_value = ?," +
                    "neighbour10 = ?, neighbour10_value = ? where id = ?";
            jdbcTemplate.update(sql, new Object[]{
                    matches.get(0).match(), convertedDistance,
                    matches.get(1).match(), convertedDistance1,
                    matches.get(2).match(), convertedDistance2,
                    matches.get(3).match(), convertedDistance3,
                    matches.get(4).match(), convertedDistance4,
                    matches.get(5).match(), convertedDistance5,
                    matches.get(6).match(), convertedDistance6,
                    matches.get(7).match(), convertedDistance7,
                    matches.get(8).match(), convertedDistance8,
                    matches.get(9).match(), convertedDistance9, vectorId});
        } else {
            sql = "insert into jos_haiku_master_word2vec_model (token, " +
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
                    matches.get(0).match(), convertedDistance,
                    matches.get(1).match(), convertedDistance1,
                    matches.get(2).match(), convertedDistance2,
                    matches.get(3).match(), convertedDistance3,
                    matches.get(4).match(), convertedDistance4,
                    matches.get(5).match(), convertedDistance5,
                    matches.get(6).match(), convertedDistance6,
                    matches.get(7).match(), convertedDistance7,
                    matches.get(8).match(), convertedDistance8,
                    matches.get(9).match(), convertedDistance9});
        }
    }

    private int convertDoubleToInt(int constant, double vectorValue) {
        return (int) (constant * vectorValue);
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