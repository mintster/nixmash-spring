package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.repository.PostRepository;
import com.nixmash.springdata.jpa.utils.PostUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by daveburke on 6/1/16.
 */
@Service
public class PostServiceImpl implements PostService{

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    @Override
    public Post add(PostDTO postDTO) {

        Post post =  postRepository.save(PostUtils.postDtoToPost(postDTO));
        return post;
    }
}
