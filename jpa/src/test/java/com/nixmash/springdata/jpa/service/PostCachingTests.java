package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.model.Post;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles(DataConfigProfile.H2)
public class PostCachingTests {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private PostService postService;

    private Cache postCache;

    @Before
    public void setup() {
        postCache = this.cacheManager.getCache("posts");
        assertThat(postCache).isNotNull();
        postCache.evict("posts");
    }

    @Test
    public void validateCacheByPostId() throws Exception {
        assertThat(postCache.get(1L)).isNull();
        Post post = postService.getPostById(1L);
        assertThat((Post) postCache.get(post.getPostId()).get()).isEqualTo(post);
    }

    @Test
    public void validateCacheByPostName() throws Exception {
        assertThat(postCache.get("a-java-collection-of-value-pairs-tuples")).isNull();
        Post post = postService.getPost("a-java-collection-of-value-pairs-tuples");
        assertThat((Post) postCache.get(post.getPostName()).get()).isEqualTo(post);
    }

    @Test
    public void compareRetrievalForAllPublishedPosts() throws Exception {
        List<Post> posts;
        long start;
        long end;

        start = timeMark();
        postService.getAllPublishedPosts();
        end = timeMark();
        System.out.println("Retrieval without cache: " + totalTime(start, end));

        start = timeMark();
        posts = postService.getAllPublishedPosts();
        end = timeMark();
        System.out.println("Retrieval WITH cache: " + totalTime(start, end));

//        assertThat(((JCacheCache) postCache).lookup(SimpleKey.EMPTY)).isEqualTo(posts);
    }

    private long timeMark() {
        return new Date().getTime();
    }

    private String totalTime(long lStartTime, long lEndTime) {
        long duration = lEndTime - lStartTime;
        String totalTime = String.format("Total time: %d milliseconds", duration);
        return totalTime;
    }

    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}