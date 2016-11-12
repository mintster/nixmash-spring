package com.nixmash.springdata.batch;

import com.nixmash.springdata.batch.config.ApplicationConfiguration;
import com.nixmash.springdata.batch.wp.ImportConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class BatchLauncher {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
//////        AnnotationConfigApplicationContext ctx = new
//////                AnnotationConfigApplicationContext("com.nixmash.springdata.batch",
//////                "com.nixmash.springdata.jpa");
        ctx.register(ApplicationConfiguration.class);
        ctx.register(ImportConfiguration.class);
        ctx.refresh();

        JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
        Job job = (Job) ctx.getBean("importPostJob");
        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addLong("time",System.currentTimeMillis()).toJobParameters();

        System.out.println("Starting the batch job");
        try {
            JobExecution execution = jobLauncher.run(job, jobParameters);
            System.out.println("Job Status : " + execution.getStatus());
            System.out.println("Job completed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Job failed");
        }

        ctx.close();
//        SpringApplication.run(BatchLauncher.class);
    }
}
