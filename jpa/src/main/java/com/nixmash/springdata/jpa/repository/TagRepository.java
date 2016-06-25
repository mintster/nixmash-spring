package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface TagRepository extends CrudRepository<Tag, Long> {

    Tag findByTagValueIgnoreCase(String tagValue);

    Set<Tag> findAll();
}
