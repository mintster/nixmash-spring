package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.GitHubStats;
import org.springframework.data.repository.CrudRepository;

public interface GitHubRepository extends CrudRepository<GitHubStats, Long> {

    GitHubStats findOne(Long statid);

    GitHubStats findTopByOrderByStatDateDesc();

}
