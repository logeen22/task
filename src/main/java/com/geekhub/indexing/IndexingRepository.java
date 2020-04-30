package com.geekhub.indexing;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

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

    public void save(Set<String> urls) {
        for (String url : urls) {
            if (check(url)) {
                save(url);
            }
        }
    }


    public boolean check(String url) {
        String sql = "SELECT * FROM url WHERE url=:url";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("url", url);
        List<Site> query = jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Site site = new Site();
            site.setId(rs.getInt("id"));
            site.setUrl(rs.getString("url"));
            return site;
        });
        return query.isEmpty();
    }

}