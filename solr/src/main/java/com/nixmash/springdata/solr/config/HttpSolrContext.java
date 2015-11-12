package com.nixmash.springdata.solr.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.repository.support.SolrRepositoryFactory;
import org.springframework.data.solr.server.support.HttpSolrServerFactoryBean;

import com.nixmash.springdata.solr.repository.SolrProductRepository;
import com.nixmash.springdata.solr.repository.custom.CustomSolrRepositoryImpl;
import com.nixmash.springdata.solr.repository.custom.DerivedSolrProductRepository;
import com.nixmash.springdata.solr.repository.factory.CustomSolrRepositoryFactoryBean;

@Configuration
@EnableSolrRepositories(basePackages = "com.nixmash.springdata.solr.repository", repositoryFactoryBeanClass = CustomSolrRepositoryFactoryBean.class)
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
	public SolrProductRepository searchRepository() throws Exception {
		SolrProductRepository searchRepository = new SolrProductRepository();
		searchRepository.setSolrOperations(solrTemplate());
		return searchRepository;
	}

	@Bean
	public DerivedSolrProductRepository derivedRepository() throws Exception {
		return new SolrRepositoryFactory(solrTemplate()).getRepository(DerivedSolrProductRepository.class,
				new CustomSolrRepositoryImpl(solrTemplate()));
	}

}
