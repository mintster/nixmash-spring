package com.nixmash.springdata.solr.repository.factory;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.repository.SolrCrudRepository;

import com.nixmash.springdata.solr.model.Product;

/**
 * 
 * NixMash Spring Notes: ---------------------------------------------------
 * 
 * Based on Petri Kainulainen's Spring Data Solr Tutorial at
 * http://www.petrikainulainen.net/spring-data-solr-tutorial/
 *
 */
@NoRepositoryBean
public interface CustomBaseRepository<T, ID extends Serializable> extends SolrCrudRepository<T, ID> {

	public long count(String searchTerm);

	public void update(Product productEntry);
}
