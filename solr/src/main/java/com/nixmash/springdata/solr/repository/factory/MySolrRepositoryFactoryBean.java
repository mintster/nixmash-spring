package com.nixmash.springdata.solr.repository.factory;

import java.io.Serializable;

import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.repository.support.SolrRepositoryFactory;
import org.springframework.data.solr.repository.support.SolrRepositoryFactoryBean;

/**
 * 
 * NixMash Spring Notes: ---------------------------------------------------
 * 
 * Based on Petri Kainulainen's Spring Data Solr Tutorial at
 * http://www.petrikainulainen.net/spring-data-solr-tutorial/
 *
 */
public class MySolrRepositoryFactoryBean extends SolrRepositoryFactoryBean {

	@Override
	protected RepositoryFactorySupport doCreateRepositoryFactory() {
		return new CustomSolrRepositoryFactory(getSolrOperations());
	}

	private static class CustomSolrRepositoryFactory<T, ID extends Serializable> extends SolrRepositoryFactory {

		private final SolrOperations solrOperations;

		public CustomSolrRepositoryFactory(SolrOperations solrOperations) {
			super(solrOperations);
			this.solrOperations = solrOperations;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			return new FactoryBaseRepositoryImpl<T, ID>(solrOperations, (Class<T>) metadata.getDomainType());
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return FactoryBaseRepository.class;
		}
	}
}
