package com.nixmash.springdata.solr.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableConfigurationProperties
@EnableSolrRepositories(
		basePackages = "com.nixmash.springdata.solr.repository", 
		namedQueriesLocation = "classpath:named-queries.properties")
@ComponentScan(basePackages = "com.nixmash.springdata.solr")
@Import({ EmbeddedSolrContext.class, HttpSolrContext.class })
@PropertySource("classpath:application.properties")
public class SolrApplicationConfig {

	@Configuration
	@Profile("dev")
	@PropertySource("classpath:application-dev.properties")
	static class DevConfig {

	}

	@Configuration
	@Profile("prod")
	@PropertySource("classpath:application-prod.properties")
	static class ProdConfig {

	}

}