package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles(DataConfigProfile.H2)
public class PostCachingTests {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void cacheNotNull() throws Exception {
        Cache postCache= this.cacheManager.getCache("posts");
        assertThat(postCache).isNotNull();

    }
}