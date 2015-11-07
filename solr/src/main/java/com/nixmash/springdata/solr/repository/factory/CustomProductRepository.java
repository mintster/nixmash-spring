package com.nixmash.springdata.solr.repository.factory;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;

import com.nixmash.springdata.solr.model.Product;

/**
 * 
 * NixMash Spring Notes: ---------------------------------------------------
 * 
 * Based on Petri Kainulainen's Spring Data Solr Tutorial at
 * http://www.petrikainulainen.net/spring-data-solr-tutorial/
 *
 */
public interface CustomProductRepository extends CustomBaseRepository<Product, String> {

	public List<Product> findByNameContains(String name, Pageable page);

	// @Query(name = "Product.findByNamedQuery")
	// public List<Product> findByNamedQuery(String searchTerm, Pageable page);

	@Query("name:*?0*")
	public List<Product> findByQueryAnnotation(String searchTerm, Pageable page);
}
