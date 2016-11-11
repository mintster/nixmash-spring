package com.nixmash.springdata.batch.wp;

import com.nixmash.springdata.jpa.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PostItemProcessor implements ItemProcessor<Post, Post> {

    private static final Logger log = LoggerFactory.getLogger(PostItemProcessor.class);

    @Override
    public Post process(final Post post) throws Exception {
        final String postTitle = post.getPostTitle().toUpperCase();

        Post transformedPost = new Post();
        transformedPost.setPostTitle(postTitle);

        log.info("Converting (" + post.getPostTitle() + ") into (" + postTitle + ")");

        return transformedPost;
    }
}