package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.dto.TagDTO;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.exceptions.DuplicatePostNameException;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.utils.PostTestUtils;
import com.nixmash.springdata.jpa.utils.PostUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static com.nixmash.springdata.jpa.utils.PostTestUtils.*;
import static com.nixmash.springdata.jpa.utils.PostUtils.postDtoToPost;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Transactional
@ActiveProfiles(DataConfigProfile.H2)
public class PostServiceTests {

    @Autowired
    PostService postService;

    @Test
    public void addPostDTO() throws DuplicatePostNameException {
        PostDTO postDTO = PostTestUtils.createPostDTO();
        Post post = postService.add(postDTO);
        assertNotNull(post);
    }

    @Test
    public void updatePostDTO() throws PostNotFoundException {
        Post post = postService.getPostById(1L);
        PostDTO postDTO = PostUtils.postToPostDTO(post);
        String newTitle = "New Title 897";
        postDTO.setPostTitle(newTitle);
        Post update = postService.update(postDTO);
        assertEquals(update.getPostTitle(), newTitle);
        assertEquals(update.getPostName(), PostUtils.createSlug(newTitle));
    }

    @Test
    public void builderShouldReturn_Null_ForMalformedLink() {
        PostDTO postDTO = PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, "malformed.link", POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
        assertEquals(postDTO.getPostSource(), null);
    }

    @Test
    public void builderShouldReturnDomainAsPostSourceFromLink() {
        PostDTO postDTO = PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, "http://wellformed.link", POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
        assertEquals(postDTO.getPostSource(), "wellformed.link");
    }

    @Test
    public void builderShouldReturn_Null_ForNullLink() {
        PostDTO postDTO = PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, null, POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
        assertEquals(postDTO.getPostSource(), null);
    }

    @Test
    public void postDtoToPostShouldRetainPostSource() {
        PostDTO postDTO = PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, "http://wellformed.link", POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
        assertEquals(postDTO.getPostSource(), "wellformed.link");
        Post post = postDtoToPost(postDTO);
        assertEquals(post.getPostSource(), "wellformed.link");
    }

    @Test
    public void postDtoToPostShouldRetainPostSourceOf_NA_ForNullLink() {
        PostDTO postDTO = PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, null, POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
        assertEquals(postDTO.getPostSource(), null);
        Post post = postDtoToPost(postDTO);
        assertEquals(post.getPostSource(), null);
    }

    @Test
    public void findAllWithPaging() {
        Slice<Post> posts = postService.getPosts(0, 3);
        assertEquals(posts.getSize(), 3);
        ZonedDateTime firstPostDate = posts.getContent().get(0).getPostDate();
        ZonedDateTime secondPostDate = posts.getContent().get(1).getPostDate();

        // firstPostDate is higher (more recent) than secondPostDate with [sort: postDate: DESC]
        assertTrue(firstPostDate.compareTo(secondPostDate) > 0);

        for (Post post : posts) {
            System.out.println(post.getPostTitle());
        }
    }

    @Test
    public void findAllWithDetails() {
        List<Post> posts = postService.getPostsWithDetail();
        assertNotNull(posts);
    }

    @Test
    public void addPostWithTags() throws DuplicatePostNameException, PostNotFoundException {
        PostDTO postDTO = PostTestUtils.createPostDTO();
        postDTO.getTags().add(new TagDTO("addPostWithTags1"));
        postDTO.getTags().add(new TagDTO("addPostWithTags2"));
        Post post = postService.add(postDTO);
        assertEquals(post.getTags().size(), 2);

        Post retrieved = postService.getPostById(post.getPostId());
        assertEquals(retrieved.getTags().size(), 2);
    }

    @Test
    public void updatePostWithTags() throws DuplicatePostNameException, PostNotFoundException {
        Post post = postService.getPostById(5L);
        PostDTO postDTO = PostUtils.postToPostDTO(post);
        postDTO.getTags().add(new TagDTO("updatePostWithTags1"));
        Post updated = postService.update(postDTO);
        assertEquals(updated.getTags().size(), 1);

        Post retrieved = postService.getPostById(5L);
        assertEquals(retrieved.getTags().size(), 1);
    }
}
