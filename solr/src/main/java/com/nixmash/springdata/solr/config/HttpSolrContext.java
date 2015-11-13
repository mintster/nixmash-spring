package com.nixmash.springdata.solr.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.support.SolrRepositoryFactory;
import org.springframework.data.solr.server.support.HttpSolrServerFactoryBean;

import com.nixmash.springdata.solr.repository.derived.DerivedBaseRepositoryImpl;
import com.nixmash.springdata.solr.repository.derived.DerivedProductRepository;
import com.nixmash.springdata.solr.repository.simple.SimpleProductRepository;

@Configuration
@Profile("prod")
public class HttpSolrContext {

	private static final String SOLR_SERVER_URL = "solr.server.url";

	@Resource
	private Environment environment;

	@Bean(name = "solrServer")
	public HttpSolrServerFactoryBean solrServerFactoryBean() {
		HttpSolrServerFactoryBean factory = new HttpSolrServerFactoryBean();
		factory.setUrl(environment.getRequiredProperty(SOLR_SERVER_URL));
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

	@Bean
	public DerivedProductRepository derivedRepository() throws Exception {
		return new SolrRepositoryFactory(solrTemplate()).getRepository(DerivedProductRepository.class,
				new DerivedBaseRepositoryImpl(solrTemplate()));
	}

}
