package com.nixmash.springdata.solr.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.repository.support.SolrRepositoryFactory;
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactoryBean;

import com.nixmash.springdata.solr.repository.SolrProductRepository;
import com.nixmash.springdata.solr.repository.custom.CustomSolrRepositoryImpl;
import com.nixmash.springdata.solr.repository.custom.DerivedSolrProductRepository;
import com.nixmash.springdata.solr.repository.factory.CustomSolrRepositoryFactoryBean;

@EnableSolrRepositories(basePackages = "com.nixmash.springdata.solr.repository", repositoryFactoryBeanClass = CustomSolrRepositoryFactoryBean.class)
@Configuration
@Profile("dev")
public class EmbeddedSolrContext {

	private static final String PROPERTY_NAME_SOLR_HOME = "solr.solr.home";

	@Resource
	private Environment environment;

	@Bean(name = "solrServer")
	public EmbeddedSolrServerFactoryBean solrServerFactoryBean() {
		EmbeddedSolrServerFactoryBean factory = new EmbeddedSolrServerFactoryBean();
		factory.setSolrHome(environment.getRequiredProperty(PROPERTY_NAME_SOLR_HOME));
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
