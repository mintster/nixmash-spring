package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.PostImage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostImageRepository extends CrudRepository<PostImage, Long> {

    List<PostImage> findAll();

    List<PostImage> findByPostId(long postId);
}
