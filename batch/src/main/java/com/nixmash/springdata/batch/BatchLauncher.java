package com.nixmash.springdata.batch;

import com.nixmash.springdata.batch.config.ApplicationConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

@SpringBootApplication
public class BatchLauncher {

    public static void main(String[] args) throws Exception {

        PropertySource commandLineProperties = new
                SimpleCommandLinePropertySource(args);

        AnnotationConfigApplicationContext context= new
                AnnotationConfigApplicationContext();

        context.getEnvironment().getPropertySources().addFirst(commandLineProperties);
        context.register(ApplicationConfiguration.class);
        context.refresh();
    }

}
