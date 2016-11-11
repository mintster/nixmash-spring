package com.nixmash.springdata.batch;

import com.nixmash.springdata.batch.wp.ImportConfiguration;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
@EnableBatchProcessing
public class BatchLauncher {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ImportConfiguration.class);
        ctx.refresh();
        ctx.close();
    }
}
