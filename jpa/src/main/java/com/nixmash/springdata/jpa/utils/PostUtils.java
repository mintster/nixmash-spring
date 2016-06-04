package com.nixmash.springdata.jpa.utils;

import com.github.slugify.Slugify;
import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by daveburke on 6/1/16.
 */
public class PostUtils {

    private static final Logger logger = LoggerFactory.getLogger(PostUtils.class);


    public static Post postDtoToPost(PostDTO dto) {

        return Post.getBuilder(dto.getUserId(),
                dto.getPostTitle(),
                dto.getPostName(),
                dto.getPostLink(),
                dto.getPostContent(),
                dto.getPostType(),
                dto.getDisplayType())
                .postSource(dto.getPostSource())
                .build();
    }

    public static String getPostSource(String url) {
        String domain = "NA";
        if (url == null)
            return domain;
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
}
