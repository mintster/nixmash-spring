package com.nixmash.springdata.batch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by daveburke on 11/12/16.
 */
@Configuration
@EnableBatchProcessing
@EnableScheduling
@ComponentScan(basePackages = {
        "com.nixmash.springdata.batch", "com.nixmash.springdata.jpa"
})
@PropertySources({
        @PropertySource("classpath:/batch.properties"),
        @PropertySource("file:/home/daveburke/web/nixmashspring/jobs.properties")
})
public class ApplicationConfiguration {
}
