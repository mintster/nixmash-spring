package com.nixmash.springdata.batch;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class BatchLauncher {


    public static void main(String[] args) throws Exception {
        ApplicationContext context= new
                AnnotationConfigApplicationContext("com.nixmash.springdata.batch",
                "com.nixmash.springdata.jpa");

//        PostImportRunner postImportRunner = context.getBean(PostImportRunner.class);
//        postImportRunner.runPostImportJob();
//        ((ConfigurableApplicationContext) context).close();

    }
}
