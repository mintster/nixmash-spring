package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.model.BatchJob;
import com.nixmash.springdata.jpa.model.GitHubStats;

import java.util.List;

/**
 * Created by daveburke on 12/2/16.
 */
public interface StatService {

    GitHubStats getCurrentGitHubStats();

    List<BatchJob> getBatchJobsByJob(String jobName);
}
