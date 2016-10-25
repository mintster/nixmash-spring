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
    public static final PostType POST_TYPE = PostType.POST;
    public static final PostDisplayType DISPLAY_TYPE = PostDisplayType.LINK;


    public static PostDTO createPostDTO(int i) {
        return PostDTO.getBuilder(USER_ID,
                fieldit(POST_TITLE, i), fieldit(POST_NAME, i), POST_LINK, POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
    }

    private static String fieldit(String field, int i) {
        return String.format("%s-%d", field, i);
    }


    public static PostDTO createPostDTO(String appender) {
        return PostDTO.getBuilder(USER_ID,
                fieldit(POST_TITLE, appender), fieldit(POST_NAME, appender), POST_LINK, POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
    }

    private static String fieldit(String field, String appender) {
        return String.format("%s-%s", field, appender);
    }
}
