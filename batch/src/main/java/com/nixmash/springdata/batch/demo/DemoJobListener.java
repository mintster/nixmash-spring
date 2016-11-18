package com.nixmash.springdata.batch.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

/**
 * Created by daveburke on 11/10/16.
 */
@Component
public class DemoJobListener extends JobExecutionListenerSupport  {

    private static final Logger logger = LoggerFactory.getLogger(DemoJobListener.class);

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("!!! JOB FINISHED! LAST POSTID IMPORTED: " + jobExecution.getExecutionContext().get("postId") );
        }
    }
}