package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {

    Tag findByTagValueIgnoreCase(String tagValue);
    List<Tag> findAll();

}
