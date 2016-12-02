package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.BatchJob;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BatchJobRepository extends CrudRepository<BatchJob, Long> {


    List<BatchJob> findByJobName(String jobName, Sort sort);

}
