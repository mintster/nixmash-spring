package com.nixmash.springdata.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:/home/daveburke/web/nixmashspring/jobs.properties")
@ConditionalOnProperty(name = "import.post.job.enable")
public class JobRunner {

    private final JobLauncher jobLauncher;
    private final Job importPostJob;

    @Value("${jobs.importPostJobParam1}")
    Integer iterations;

    @Value("${jobs.importPostJobParam2}")
    String username;

    @Autowired
    public JobRunner(JobLauncher jobLauncher, Job importPostJob) {
        this.jobLauncher = jobLauncher;
        this.importPostJob = importPostJob;
    }

    @Scheduled(fixedRate = 5000)
    public void runImportPostJob() {
        System.out.println();
        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addString("iterations", String.valueOf(iterations))
                        .addString("username", username)
                        .addLong("time", System.currentTimeMillis()).toJobParameters();

        try {
            System.out.println("STARTING BATCH JOB!!!");
            JobExecution execution = jobLauncher.run(importPostJob, jobParameters);
            System.out.println("JOB STATUS : " + execution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("JOB FAILED!!!");
        }

    }

}
