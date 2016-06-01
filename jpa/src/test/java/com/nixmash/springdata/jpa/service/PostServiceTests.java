package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.utils.PostTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.nixmash.springdata.jpa.utils.PostTestUtils.*;
import static com.nixmash.springdata.jpa.utils.PostUtils.postDtoToPost;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Transactional
@ActiveProfiles(DataConfigProfile.H2)
public class PostServiceTests {

    private static final String NA = "NA";

    @Autowired
    PostService postService;

    @Test
    public void addPostDTO() {
        PostDTO postDTO = PostTestUtils.createPostDTO();
        Post post = postService.add(postDTO);
        assertNotNull(post);
    }

    @Test
    public void builderShouldReturn_NA_ForMalformedLink() {
        PostDTO postDTO = PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, "malformed.link", POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
        assertEquals(postDTO.getPostSource(), NA);
    }

    @Test
    public void builderShouldReturnDomainAsPostSourceFromLink() {
        PostDTO postDTO = PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, "http://wellformed.link", POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
        assertEquals(postDTO.getPostSource(), "wellformed.link");
    }

    @Test
    public void builderShouldReturn_NA_ForNullLink() {
        PostDTO postDTO = PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, null, POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
        assertEquals(postDTO.getPostSource(), NA);
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
        assertEquals(postDTO.getPostSource(), NA);
        Post post = postDtoToPost(postDTO);
        assertEquals(post.getPostSource(), NA);
    }

}
