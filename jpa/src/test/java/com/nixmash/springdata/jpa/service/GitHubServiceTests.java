package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.repository.GitHubRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

/**
 * Created by daveburke on 12/2/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Transactional
@ActiveProfiles(DataConfigProfile.H2)
public class GitHubServiceTests {

    @Autowired
    GitHubRepository gitHubRepository;

    @Autowired
    StatService statService;

    @Test
    public void getGitHubStatsByIdFromRepo() throws Exception {
        assertNotNull(gitHubRepository.findOne(1L));
    }

    @Test
    public void getCurrentGitHubStatsFromRepo() throws Exception {
        assertNotNull(gitHubRepository.findTopByOrderByStatDateDesc());
    }

    @Test
    public void getCurrentGitHubStatsFromService() throws Exception {
        System.out.println(statService.getCurrentGitHubStats());
    }


}
