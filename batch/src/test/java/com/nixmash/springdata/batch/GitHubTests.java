package com.nixmash.springdata.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("h2")
public class GitHubTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void githubRecordRetrievedByDate() throws Exception {
        // GithubStat record with '2016-12-01' in H2
        long statId = jdbcTemplate.queryForObject("SELECT stat_id FROM github_stats WHERE stat_date = '2016-12-01'", Long.class);
        assertThat(statId).isEqualTo(1L);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void githubStatNonExistingDateRecordThrowsException() throws Exception {
        long statId = jdbcTemplate.queryForObject("SELECT stat_id FROM github_stats WHERE stat_date = '2012-12-12'", Long.class);
    }

    @Test
    public void githubStatRecordCreated() throws Exception {
        // GithubStat record dated '2011-11-11' doesn't exist. Exception thrown, new record created
        long statId = -1L;
        try {
            statId = jdbcTemplate.queryForObject("SELECT stat_id FROM github_stats WHERE stat_date = '2011-11-11'", Long.class);
        } catch (EmptyResultDataAccessException e) {
            jdbcTemplate.update("INSERT INTO github_stats (stat_date) VALUES ('2011-11-11')");
        }

        // repeat retrieval for GithubStats dated '2011-11-11'
        assertThat(statId).isEqualTo(-1L);
        statId = jdbcTemplate.queryForObject("SELECT stat_id FROM github_stats WHERE stat_date = '2011-11-11'", Long.class);
        assertThat(statId).isGreaterThan(0);
    }

    @Test
    public void githubStatRecordKeyReturned() throws Exception {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        long statId = -1L;
        try {
            statId = jdbcTemplate.queryForObject("SELECT stat_id FROM github_stats WHERE stat_date = '2010-10-10'", Long.class);
        } catch (EmptyResultDataAccessException e) {

            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        String INSERT_SQL = "INSERT INTO github_stats (stat_date) VALUES ('2010-10-10')";

                        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"stat_id"});
                            return ps;
                        }
                    },
                    keyHolder);
            statId = keyHolder.getKey().longValue();
        }

        assertThat(statId).isGreaterThan(0);
    }

}