package com.nixmash.springdata.solr.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactoryBean;

import com.nixmash.springdata.solr.common.SolrSettings;
import com.nixmash.springdata.solr.repository.simple.SimpleProductRepository;

@Configuration
@Profile("dev")
public class EmbeddedSolrContext {


	@Resource
	private Environment environment;

	@Autowired
	private SolrSettings solrSettings;
	
	@Bean(name = "solrServer")
	public EmbeddedSolrServerFactoryBean solrServerFactoryBean() {
		EmbeddedSolrServerFactoryBean factory = new EmbeddedSolrServerFactoryBean();
		factory.setSolrHome(solrSettings.getSolrEmbeddedPath());
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
