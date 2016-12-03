package com.nixmash.springdata.batch.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Conditional(DemoJobCondition.class)
public class DemoJobRunner {

    private static final Logger logger = LoggerFactory.getLogger(DemoJobRunner.class);

    private final JobLauncher jobLauncher;
    private final Job demoJob;

    @Value("${demo.job.param.iterations}")
    long iterations;

    @Value("${demo.job.param.username}")
    String username;

    @Autowired
    public DemoJobRunner(JobLauncher jobLauncher, Job demoJob) {
        this.jobLauncher = jobLauncher;
        this.demoJob = demoJob;
    }

    @Scheduled(fixedDelayString = "${demo.job.fixed.delay.seconds:60}000")
    public void runDemoJob() {

        SimpleDateFormat format = new SimpleDateFormat("M-dd-yy hh:mm:ss");
        String startDateTime = format.format(new Date());

        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addLong("iterations", iterations)
                        .addString("username", username)
                        .addLong("time", System.currentTimeMillis()).toJobParameters();

        try {
            logger.info("");
            logger.info("STARTING BATCH JOB AT " + startDateTime);
            JobExecution execution = jobLauncher.run(demoJob, jobParameters);
            logger.info("JOB STATUS : " + execution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("JOB FAILED!!!");
        }

    }

}
