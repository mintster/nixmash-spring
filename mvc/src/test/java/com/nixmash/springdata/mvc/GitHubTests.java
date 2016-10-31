package com.nixmash.springdata.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nixmash.springdata.jpa.dto.GitHubDTO;
import com.nixmash.springdata.mvc.components.WebUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;

import static com.nixmash.springdata.jpa.utils.SharedUtils.timeMark;
import static com.nixmash.springdata.jpa.utils.SharedUtils.totalTime;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
public class GitHubTests extends AbstractContext {

    private static final Logger logger = LoggerFactory.getLogger(GitHubTests.class);


    @Autowired
    Environment environment;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private WebUI webUI;

    private String gitHubRepoUrl;
    private String gitHubUserUrl;
    private TestRestTemplate restTemplate;
    private Cache githubCache;

    @Before
    public void setUp() {
        gitHubRepoUrl = environment.getProperty("github.repo.url");
        gitHubUserUrl = environment.getProperty("github.user.url");
        restTemplate = new TestRestTemplate();
        githubCache = this.cacheManager.getCache("githubStats");
    }

    @After
    public void tearDown() {
        githubCache.evict("githubStats");
    }

    @Test(expected = RestClientException.class)
    public void badGitHubUrl() throws Exception {
        restTemplate.getForObject("http://bad.url", GitHubDTO.class);
    }

    @Test
    public void returnEmptyGithubDTO() throws Exception {
        GitHubDTO gitHubDTO = new GitHubDTO();
        assert(gitHubDTO.getIsEmpty().equals(false));

        try {
            gitHubDTO = restTemplate.getForObject("http://bad.url", GitHubDTO.class);
        } catch (RestClientException e) {
            gitHubDTO.setIsEmpty(true);
        }
        assert(gitHubDTO.getIsEmpty().equals(true));
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
        assertThat(gitHubDTO.getFollowers(), greaterThan(-1));

    }

    @Test
    public void validateGithubStatsCache() throws Exception {
        assertNotNull(githubCache);
        GitHubDTO gitHubDTO = webUI.getGitHubStats();
        githubCache.get("getGitHubStats");
        assertThat(
                ((GitHubDTO) githubCache.get("getGitHubStats").get()).getFollowers(), greaterThan(-1));
    }

    @Test
    public void githubStatsAccessTimes() {
        long start;
        long end;

        githubCache.clear();

        start = timeMark();
        webUI.getGitHubStats();
        end = timeMark();
        System.out.println("Github Stats without Caching: " + totalTime(start, end));

        start = timeMark();
        webUI.getGitHubStats();
        end = timeMark();
        System.out.println("Github Stats WITH Caching: " + totalTime(start, end));

    }
}
