package com.nixmash.springdata.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nixmash.springdata.jpa.dto.GitHubDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
public class GitHubTests extends AbstractContext {

    @Autowired
    Environment environment;

    private String gitHubRepoUrl;
    private String gitHubUserUrl;
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        gitHubRepoUrl = environment.getProperty("github.repo.url");
        gitHubUserUrl = environment.getProperty("github.user.url");
        restTemplate = new TestRestTemplate();
    }

    @Test
    public void getGitHubProjectStatsInStringResponseEntity() throws Exception {

        HttpEntity<String> stringRepoEntity = restTemplate.getForEntity(gitHubRepoUrl, String.class);
        HttpEntity<String> stringUserEntity = restTemplate.getForEntity(gitHubUserUrl, String.class);

        assertEquals(stringRepoEntity.getHeaders().getContentType(), MediaType.APPLICATION_JSON_UTF8);
        assertNotNull(jsonPath("$.watchers_count"));

        ObjectNode node;
        ObjectMapper mapper = new ObjectMapper();
        node = mapper.readValue(stringRepoEntity.getBody(), ObjectNode.class);
        Integer watchers = node.get("watchers_count").intValue();
        assertThat(watchers, greaterThan(-1));

        node = mapper.readValue(stringUserEntity.getBody(), ObjectNode.class);
        Integer followers = node.get("followers").intValue();
        assertThat(followers, greaterThan(-1));

    }

    @Test
    public void getGitHubProjectStatsInClassObject() throws Exception {
        GitHubDTO gitHubDTO = restTemplate.getForObject(gitHubRepoUrl, GitHubDTO.class);
        assertThat(gitHubDTO.getSubscribers(), greaterThan(-1));

        HttpEntity<String> stringUserEntity = restTemplate.getForEntity(gitHubUserUrl, String.class);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue(stringUserEntity.getBody(), ObjectNode.class);
        gitHubDTO.setFollowers(node.get("followers").intValue());

        System.out.println(gitHubDTO.toString());
    }
}
