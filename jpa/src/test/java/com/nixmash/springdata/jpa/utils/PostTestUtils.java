package com.nixmash.springdata.jpa.utils;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.enums.PostDisplayType;
import com.nixmash.springdata.jpa.enums.PostType;

/**
 * Created by daveburke on 6/1/16.
 */
public class PostTestUtils {

    public static final Long USER_ID = 1L;
    public static final String POST_TITLE = "Post title";
    public static final String POST_NAME = "post-title";
    public static final String POST_LINK = "http://test.link";
    public static final String POST_CONTENT = "Post content.";
    public static final PostType POST_TYPE = PostType.NOTE;
    public static final PostDisplayType DISPLAY_TYPE = PostDisplayType.LINK;


    public static PostDTO createPostDTO() {
        return PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, POST_LINK, POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
    }
}
