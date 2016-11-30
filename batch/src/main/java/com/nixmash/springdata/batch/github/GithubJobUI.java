package com.nixmash.springdata.batch.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nixmash.springdata.jpa.dto.GitHubDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Created by daveburke on 11/30/16.
 */
@SuppressWarnings("Duplicates")
@Component
public class GithubJobUI {

    private static final Logger logger = LoggerFactory.getLogger(GithubJobUI.class);

    @Autowired
    Environment environment;

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
