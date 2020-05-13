package com.task.indexing;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class IndexingRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public IndexingRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String url) {
        String sql = "INSERT INTO url (url) VALUES(:url)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("url", url);

        jdbcTemplate.update(sql, params);
    }

    public boolean isLinkAvailableInDatabase(String url) {
        String sql = "SELECT count(id) FROM url WHERE url=:url";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("url", url);
        Integer integer = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return integer.equals(0);
    }
}
