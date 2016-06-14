package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.Post;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by daveburke on 5/31/16.
 */
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    Post findByPostId(Long postId) throws DataAccessException;

    Post findByPostNameIgnoreCase(String postName) throws DataAccessException;
}
