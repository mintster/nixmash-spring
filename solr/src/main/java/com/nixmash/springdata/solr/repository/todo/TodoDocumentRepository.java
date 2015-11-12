package com.nixmash.springdata.solr.repository.todo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.solr.repository.Query;
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
public interface TodoDocumentRepository extends PartialUpdateRepository, SolrCrudRepository<Product, String> {

	public List<Product> findByNameContainsOrCategoriesContains(String title, String category, Sort sort);

	@Query(name = "Product.findByNamedQuery")
	public List<Product> findByNamedQuery(String searchTerm, Sort sort);

	@Query("name:*?0* OR cat:*?0*")
	public List<Product> findByQueryAnnotation(String searchTerm, Sort sort);
}
