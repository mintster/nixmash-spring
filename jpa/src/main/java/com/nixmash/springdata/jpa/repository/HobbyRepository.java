package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.Hobby;
import org.springframework.data.repository.CrudRepository;

public interface HobbyRepository extends CrudRepository<Hobby, Long> {

    Hobby findByHobbyTitleIgnoreCase(String hobbyTitle);
//    Contact findByEmail(String email);
}
