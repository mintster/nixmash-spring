package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.exceptions.DuplicatePostNameException;
import com.nixmash.springdata.jpa.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by daveburke on 6/1/16.
 */
public interface PostService {

    public Post add(PostDTO postDTO) throws DuplicatePostNameException;

    @Transactional(readOnly = true)
    Post getPost(String postName);

    @Transactional(readOnly = true)
    Page<Post> getPosts(Integer pageNumber, Integer pageSize);
}
