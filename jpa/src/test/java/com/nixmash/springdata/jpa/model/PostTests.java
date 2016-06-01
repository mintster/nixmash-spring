package com.nixmash.springdata.jpa.model;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.repository.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by daveburke on 5/31/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles(DataConfigProfile.H2)
public class PostTests {

    @Autowired
    PostRepository postRepository;

    @Test
    public void nonextistentPostFromRepository() {
        Post post = postRepository.findByPostId(-1L);
        assertNull(post);
    }

    @Test
    public void retrievePostFromRepository() {
        Post post = postRepository.findByPostId(1L);
        assertNotNull(post);
    }

    @Test
    public void addPost() {
        Post post = Post.getBuilder(1L, "New Title", "new-title", "http://some.link", "New post content!").build();
        Post saved = postRepository.save(post);
        assertNotNull(saved);
        System.out.println(saved.toString());
    }
}
