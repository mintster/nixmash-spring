package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.exceptions.DuplicatePostNameException;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by daveburke on 6/1/16.
 */
public interface PostService {

    Post add(PostDTO postDTO) throws DuplicatePostNameException;

    Post getPost(String postName) throws PostNotFoundException;

    Page<Post> getPosts(Integer pageNumber, Integer pageSize);

    @Transactional
    Post update(PostDTO postDTO) throws PostNotFoundException;

    Post getPostById(Long postId) throws PostNotFoundException;

    boolean canUpdatePost(Authentication authentication, Long postId);
}
