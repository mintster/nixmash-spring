package com.nixmash.springdata.batch.demo;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class DemoJobItemProcessor implements ItemProcessor<Post, PostDTO> {

    private static final Logger log = LoggerFactory.getLogger(DemoJobItemProcessor.class);

    @Override
    public PostDTO process(final Post post) throws Exception {
        final String postTitle = post.getPostTitle().toUpperCase();

        PostDTO transformedPost = new PostDTO();
        transformedPost.setPostTitle(postTitle);

//        log.info("Converting (" + post.getPostTitle() + ") into (" + postTitle + ")");

        return transformedPost;
    }
}