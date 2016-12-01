package com.nixmash.springdata.batch.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nixmash.springdata.jpa.dto.GitHubDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by daveburke on 11/30/16.
 */
@SuppressWarnings("Duplicates")
@Component
public class GithubJobUI {

    private static final Logger logger = LoggerFactory.getLogger(GithubJobUI.class);

    private final Environment environment;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GithubJobUI(Environment environment, JdbcTemplate jdbcTemplate) {
        this.environment = environment;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveGithubStats(GitHubDTO gitHubDTO) {
        jdbcTemplate.update(
                "update github_stats set forks = ?, stars = ?, subscribers = ?, followers = ? where stat_id = ?",
                gitHubDTO.getForks(),
                gitHubDTO.getStars(),
                gitHubDTO.getSubscribers(),
                gitHubDTO.getFollowers(),
                gitHubDTO.getStatId());
    }

    public long getCurrentGithubId() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "SELECT stat_id FROM github_stats WHERE stat_date = current_date()";
        long statId = -1;
        try {
            statId = jdbcTemplate.queryForObject(sql, Long.class);
        } catch (EmptyResultDataAccessException e) {

            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        String INSERT_SQL = "INSERT INTO github_stats (stat_date) VALUES (current_date())";
                        public PreparedStatement createPreparedStatement(Connection cn) throws SQLException {
                            PreparedStatement ps = cn.prepareStatement(INSERT_SQL, new String[] {"stat_id"});
                            return ps;
                        }
                    },
                    keyHolder);
            statId = keyHolder.getKey().longValue();

        }
        logger.info("Current GitHub Stats ID: " + statId);
        return statId;
    }

    public GitHubDTO getGitHubStats() {

        String gitHubRepoUrl = environment.getProperty("github.job.repo.url");
        String gitHubUserUrl = environment.getProperty("github.job.user.url");

        // Load Repository JSON elements into GitHubDTO Object

        GitHubDTO gitHubDTO = new GitHubDTO();

        RestTemplate restTemplate = new RestTemplate();
        try {
            gitHubDTO = restTemplate.getForObject(gitHubRepoUrl, GitHubDTO.class);
        } catch (RestClientException e) {
            gitHubDTO.setIsEmpty(true);
            return gitHubDTO;
        }


        // Load User Followers count from GitHub User JSON Endpoint and add to GitHubDTO

        HttpEntity<String> stringUserEntity = restTemplate.getForEntity(gitHubUserUrl, String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectNode node = mapper.readValue(stringUserEntity.getBody(), ObjectNode.class);
            gitHubDTO.setFollowers(node.get("followers").intValue());
        } catch (IOException e) {
            logger.error("Error adding follower count from GitHub API to GitHubDTO object");
        }
        return gitHubDTO;
    }

    public GitHubDTO getDummyStats() {
        GitHubDTO gitHubDTO = new GitHubDTO();
        gitHubDTO.setStars(251);
        gitHubDTO.setForks(87);
        gitHubDTO.setSubscribers(45);
        gitHubDTO.setFollowers(87);
        return gitHubDTO;
    }
}
