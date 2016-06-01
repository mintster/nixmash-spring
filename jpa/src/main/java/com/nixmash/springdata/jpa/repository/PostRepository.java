package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.Post;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by daveburke on 5/31/16.
 */
public interface PostRepository extends CrudRepository<Post, Long> {

    Post findByPostId(Long postId) throws DataAccessException;

}
