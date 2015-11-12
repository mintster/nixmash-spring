package com.nixmash.springdata.solr.repository.todo;

import com.nixmash.springdata.solr.model.Product;

/**
 * 
 * NixMash Spring Notes: ---------------------------------------------------
 * 
 * Based on Petri Kainulainen's Spring Data Solr Tutorial at
 * http://www.petrikainulainen.net/spring-data-solr-tutorial/
 *
 */
public interface PartialUpdateRepository {

	public void update(Product product);
}
