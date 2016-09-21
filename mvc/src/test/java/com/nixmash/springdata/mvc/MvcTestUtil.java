package com.nixmash.springdata.mvc;

import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.mvc.dto.JsonPostDTO;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MvcTestUtil {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON_UTF8.getType(),
            MediaType.APPLICATION_JSON_UTF8.getSubtype(),
            Charset.forName("utf8")
    );

    public static JsonPostDTO  createJsonPostDTO(Post post) {
        Set<String> tags = new HashSet<>(Arrays.asList("tagone", "tagtwo"));
        return new JsonPostDTO(post.getPostId(),
                tags, post.getPostTitle(),
                post.getPostContent(),
                post.getIsPublished(),
                String.valueOf(post.getDisplayType()));
    }


}