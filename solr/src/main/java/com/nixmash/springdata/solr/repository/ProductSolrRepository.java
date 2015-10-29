package com.nixmash.springdata.solr.repository;

import java.util.List;

import com.nixmash.springdata.solr.model.Product;

public interface ProductSolrRepository {

	public List<Product> search(String searchTerm);

}
