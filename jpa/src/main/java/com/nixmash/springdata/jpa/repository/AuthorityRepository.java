package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    Authority findByAuthority(String authority);
}
