package com.nixmash.springdata.batch;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class BatchLauncher {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context= new
                AnnotationConfigApplicationContext("com.nixmash.springdata.batch",
                "com.nixmash.springdata.jpa");
    }
}
