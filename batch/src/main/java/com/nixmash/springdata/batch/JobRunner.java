package com.nixmash.springdata.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobRunner {

    private final JobLauncher jobLauncher;
    private final Job importPostJob;

    @Autowired
    public JobRunner(JobLauncher jobLauncher, Job importPostJob) {
        this.jobLauncher = jobLauncher;
        this.importPostJob = importPostJob;
    }

    @Scheduled(fixedRate = 5000)
    public void runImportJob() {
        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis()).toJobParameters();

        try {
            System.out.println("STARTING BATCH JOB ----------------------- */");
            JobExecution execution = jobLauncher.run(importPostJob, jobParameters);
            System.out.println("JOB STATUS : " + execution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("JOB FAILED!!!");
        }

    }

}
