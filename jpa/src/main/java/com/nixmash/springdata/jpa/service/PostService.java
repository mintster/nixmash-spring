package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.model.Post;

/**
 * Created by daveburke on 6/1/16.
 */
public interface PostService {

    public Post add(PostDTO postDTO);

}
