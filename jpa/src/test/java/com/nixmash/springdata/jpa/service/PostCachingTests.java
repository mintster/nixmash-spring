package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.utils.PostTestUtils;
import com.nixmash.springdata.jpa.utils.PostUtils;
import org.junit.After;
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
import org.springframework.transaction.annotation.Transactional;

import static com.nixmash.springdata.jpa.utils.PostTestUtils.POST_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles(DataConfigProfile.H2)
@Transactional
public class PostCachingTests {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private PostService postService;

    private Cache postCache;
    private Cache pagedPostsCache;

    @Before
    public void setup() {
        postCache = this.cacheManager.getCache("posts");
        pagedPostsCache = this.cacheManager.getCache("pagedPosts");
    }

    @After
    public void evictCaches() {
        postCache.evict("posts");
        pagedPostsCache.evict("pagedPosts");
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
    public void savedPostIsRetrievedFromCache() throws Exception {
        String appender = "post-cache";
        PostDTO postDTO = PostTestUtils.createPostDTO(appender);
        String savedPostName = String.format("%s-%s", POST_NAME ,appender);

        assertThat(postCache.get(savedPostName)).isNull();

        Post post = postService.add(postDTO);
        long postId = post.getPostId();
        String postName = post.getPostName();

        assertNull(postCache.get(post.getPostName()));
        assertNull(postCache.get(postId));

        Post postById = postService.getPostById(postId);
        Post postByName = postService.getPost(postName);

        assertThat((Post) postCache.get(postName).get()).isEqualTo(postByName);
        assertThat((Post) postCache.get(postId).get()).isEqualTo(postById);
    }

    @Test
    public void updatedPostIsRetrievedFromCache() throws Exception {

        // Confirm our pagedPosts Cache is populated

        postService.getPublishedPosts(0,10);
        assertThat(pagedPostsCache.get("0-10")).isNotNull();

        // Update Post Title and call Post Update Service

        String newTitle = "Something Wonderful";
        Post post = postService.getPostById(1L);
        String originalPostName = post.getPostName();
        PostDTO postDTO = PostUtils.postToPostDTO(post);
        postDTO.setPostTitle(newTitle);
        postService.update(postDTO);

        // Updated Post with New Title in Post Caches by Name and PostId
        // AFTER it is evicted - Updated v0.4.5

        assertNull(postCache.get(post.getPostName()));

        postService.getPost(originalPostName);
        Post postByName = (Post) postCache.get(post.getPostName()).get();
        assertThat(postByName.getPostTitle()).isEqualTo(newTitle);

        assertNull(postCache.get(post.getPostId()));

        postService.getPostById(1L);
        Post postById = (Post) postCache.get(post.getPostId()).get();
        assertThat(postById.getPostTitle()).isEqualTo(newTitle);

        // Paged Posts cache evicted on Post Update, rebuilt on next call

        assertThat(pagedPostsCache.get("0-10")).isNull();
        postService.getPublishedPosts(0,10);
        assertThat(pagedPostsCache.get("0-10")).isNotNull();
    }

}