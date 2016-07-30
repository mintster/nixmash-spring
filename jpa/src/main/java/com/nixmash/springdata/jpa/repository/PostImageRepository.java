package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.Image;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostImageRepository extends CrudRepository<Image, Long> {

    List<Image> findAll();
}
