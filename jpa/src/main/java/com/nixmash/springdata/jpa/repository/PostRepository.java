package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.Post;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by daveburke on 5/31/16.
 */
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    Post findByPostId(Long postId) throws DataAccessException;

    @Query("select distinct p from Post p left join fetch p.tags t")
    List<Post> findAllWithDetail();

//    Select distinct d from Distributor d join d.towns t join t.district t where t.name = :name
    Post findByPostNameIgnoreCase(String postName) throws DataAccessException;
}
