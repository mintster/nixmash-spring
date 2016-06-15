package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.exceptions.DuplicatePostNameException;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.Post;
import org.springframework.data.domain.Page;

/**
 * Created by daveburke on 6/1/16.
 */
public interface PostService {

    Post add(PostDTO postDTO) throws DuplicatePostNameException;

    Post getPost(String postName) throws PostNotFoundException;

    Page<Post> getPosts(Integer pageNumber, Integer pageSize);

    Post getPostById(Long postId) throws PostNotFoundException;
}
