package com.nixmash.springdata.solr.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.support.HttpSolrServerFactoryBean;

import com.nixmash.springdata.solr.common.SolrSettings;
import com.nixmash.springdata.solr.repository.simple.SimpleProductRepository;

@Configuration
@Profile("prod")
public class HttpSolrContext {


	@Resource
	private Environment environment;
	
	@Autowired
	private SolrSettings solrSettings;

	@Bean(name = "solrServer")
	public HttpSolrServerFactoryBean solrServerFactoryBean() {
		HttpSolrServerFactoryBean factory = new HttpSolrServerFactoryBean();
		factory.setUrl(solrSettings.getSolrServerUrl());
		return factory;
	}

	@Bean
	public SolrTemplate solrTemplate() throws Exception {
		return new SolrTemplate(solrServerFactoryBean().getObject());
	}

	@Bean
	public SimpleProductRepository simpleRepository() throws Exception {
		SimpleProductRepository simpleRepository = new SimpleProductRepository();
		simpleRepository.setSolrOperations(solrTemplate());
		return simpleRepository;
	}

}
