package com.nixmash.springdata.jpa.utils;

import com.github.slugify.Slugify;
import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.Post;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by daveburke on 6/1/16.
 */
public class PostUtils {

    private static final Logger logger = LoggerFactory.getLogger(PostUtils.class);
    private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

    public static Post postDtoToPost(PostDTO dto) {

        return Post.getBuilder(dto.getUserId(),
                dto.getPostTitle(),
                dto.getPostName(),
                dto.getPostLink(),
                dto.getPostContent(),
                dto.getPostType(),
                dto.getDisplayType())
                .postSource(dto.getPostSource())
                .postImage(dto.getPostImage())
                .build();
    }

    public static String createPostSource(String url) {
        String domain = null;
        if (StringUtils.isEmpty(url))
            return null;
        else {
            try {
                URL linkURL = new URL(url);
                domain = linkURL.getHost();
            } catch (MalformedURLException e) {
                logger.error("Malformed Url: " + e.getMessage());
            }
        }
        return domain;
    }

    public static String createSlug(String title) {
        Slugify slugify;
        String slug = null;
        try {
            slugify = new Slugify();
            slug =  slugify.slugify(title);
        } catch (IOException e) {
            logger.error(String.format("IOException for title: %s -- Exception: %s",title, e.getMessage()));
        }
       return slug;
    }

    public static String removeTags(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }
        Matcher m = REMOVE_TAGS.matcher(string);
        return m.replaceAll("");
    }

    public static Boolean isPostOwner(CurrentUser currentUser, Long userId) {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getId().equals(userId);
    }
}
