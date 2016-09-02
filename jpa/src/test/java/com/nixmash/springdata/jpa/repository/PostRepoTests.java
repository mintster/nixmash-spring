package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.enums.PostDisplayType;
import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.model.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.*;

/**
 * Created by daveburke on 5/31/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles(DataConfigProfile.H2)
public class PostRepoTests {

    @Autowired
    PostRepository postRepository;

    @Autowired
    TagRepository tagRepository;

    @PersistenceContext
    private EntityManager em;

    @Before
    public void setUp() {
    }

    @Test
    public void nonextistentPostFromRepository() {
        Post post = postRepository.findByPostId(-100L);
        assertNull(post);
    }

    @Test
    public void retrievePostFromRepository() {
        Post post = postRepository.findByPostId(1L);
        assertNotNull(post);
    }

    @Test
    public void addPost() {
        Post post = Post.getBuilder(1L, "New Title", "new-title", "http://some.link", "New post content!", PostType.POST, PostDisplayType.POST).build();
        Post saved = postRepository.save(post);
        assertNotNull(saved);
        assertEquals(saved.getPostType(), PostType.POST);

        // postSource is domain of url passed to builder
        assertEquals(saved.getPostSource(), "some.link");
    }

    @Test
    public void nullPostLinkEnteredAndResultsInPostSourceAsNull() {
        Post post = Post.getBuilder(1L, "Nuther New Title", "nuther-new-title", null, "New post content!", PostType.POST, PostDisplayType.POST).build();
        Post saved = postRepository.save(post);
        assertNotNull(saved);
        assertNull(saved.getPostLink());
        assertEquals(saved.getPostSource(), null);
    }

    @Test
    public void savePostWithTags() {
        Post post = Post.getBuilder(1L,
                "Post With Tags",
                "post-with-tags",
                null,
                "New post with tags!",
                PostType.POST,
                PostDisplayType.POST)
                .build();

        Tag tag1 = new Tag("third tag");
        tag1 = tagRepository.save(tag1);

        Tag tag2 = new Tag("fourth tag");
        tag2 = tagRepository.save(tag2);

        Post saved = postRepository.save(post);

        saved.setTags(new HashSet<>());
        saved.getTags().add(tag1);
        saved.getTags().add(tag2);
        assertEquals(saved.getTags().size(), 2);

        postRepository.save(saved);

        List<Post> posts= postRepository.findAllWithDetail();
        Optional<Post> found = posts.stream()
                .filter(p -> p.getPostId().equals(saved.getPostId())).findFirst();

        if (found.isPresent()) {
            assertEquals(found.get().getTags().size(), 2);
        }
    }

    @Test
    public void addTags() {

        Integer startTagCount = tagRepository.findAll().size();

        Tag tag = new Tag("tag one");
        tagRepository.save(tag);

        tag = new Tag("tag two ");
        tagRepository.save(tag);

        Set<Tag> found = tagRepository.findAll();
        assertEquals(found.size(), startTagCount + 2);
    }

    @Test
    public void alphaLinksStringShouldBeAString() {
        assertThat(postRepository.getAlphaLinkString(), isA(String.class));
    }
}
