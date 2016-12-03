package com.nixmash.springdata.batch.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by daveburke on 11/30/16.
 */
@Component
@Conditional(GithubJobCondtion.class)
public class GithubJobRunner {

    private static final Logger logger = LoggerFactory.getLogger(GithubJobRunner.class);

    private final JobLauncher jobLauncher;
    private final Job githubJob;

    public GithubJobRunner(JobLauncher jobLauncher, Job githubJob) {
        this.jobLauncher = jobLauncher;
        this.githubJob = githubJob;
    }

    @Scheduled(fixedRateString = "${github.job.fixed.delay.seconds:60}000")
    public void runGithubJob() {

        SimpleDateFormat format = new SimpleDateFormat("M-dd-yy hh:mm:ss");
        String startDateTime = format.format(new Date());

        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis()).toJobParameters();

        try {
            logger.info("");
            logger.info("STARTING GITHUB BATCH JOB : " + startDateTime);
            JobExecution execution = jobLauncher.run(githubJob, jobParameters);
            logger.info("JOB STATUS  : " + execution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("JOB FAILED!!!");
        }

    }


}
