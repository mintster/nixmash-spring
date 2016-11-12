package com.nixmash.springdata.batch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by daveburke on 11/12/16.
 */
@Configuration
@EnableBatchProcessing
@ComponentScan(basePackages = {"com.nixmash.springdata.batch", "com.nixmash.springdata.jpa"})
public class ApplicationConfiguration {
}
