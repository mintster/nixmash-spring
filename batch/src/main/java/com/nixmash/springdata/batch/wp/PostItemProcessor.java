package com.nixmash.springdata.batch.wp;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PostItemProcessor implements ItemProcessor<Post, PostDTO> {

    private static final Logger log = LoggerFactory.getLogger(PostItemProcessor.class);

    @Override
    public PostDTO process(final Post post) throws Exception {
        final String postTitle = post.getPostTitle().toUpperCase();

        PostDTO transformedPost = new PostDTO();
        transformedPost.setPostTitle(postTitle);

//        log.info("Converting (" + post.getPostTitle() + ") into (" + postTitle + ")");

        return transformedPost;
    }
}