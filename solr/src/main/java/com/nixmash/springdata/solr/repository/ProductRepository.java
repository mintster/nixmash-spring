package com.nixmash.springdata.solr.repository;

import java.util.List;

import org.springframework.data.solr.repository.SolrCrudRepository;

import com.nixmash.springdata.solr.model.Product;

public interface ProductRepository extends ProductSolrRepository, SolrCrudRepository<Product, String> {

	public List<Product> search(String searchTerm);
}
