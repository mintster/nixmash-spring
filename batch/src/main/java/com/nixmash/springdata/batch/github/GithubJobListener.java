package com.nixmash.springdata.batch.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

/**
 * Created by daveburke on 11/10/16.
 */
@Component
public class GithubJobListener extends JobExecutionListenerSupport  {

    private static final Logger logger = LoggerFactory.getLogger(GithubJobListener.class);

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            long statId = (long) jobExecution.getExecutionContext().get("statId");
            logger.info("!!! GITHUB JOB FINISHED! CURRENT STATID: " +  statId);

            ExitStatus es = jobExecution.getExitStatus();
            jobExecution.setExitStatus(new ExitStatus(es.getExitCode(), String.valueOf(statId)));
        }
    }
}