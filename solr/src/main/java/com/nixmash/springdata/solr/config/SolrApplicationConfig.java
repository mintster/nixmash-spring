package com.nixmash.springdata.solr.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = "com.nixmash.springdata.solr")
@PropertySource("classpath:application.properties")
public class SolrApplicationConfig {


}