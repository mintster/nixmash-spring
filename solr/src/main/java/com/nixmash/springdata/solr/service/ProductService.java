package com.nixmash.springdata.solr.service;

import com.nixmash.springdata.solr.model.Product;

public interface ProductService {

	public Iterable<Product> displayAvailable();

	Iterable<Product> displayAllRecords();

	Iterable<Product> displayByNameOrCategory(String searchTerm);

	Product getProduct(String Id);

	void updateProductName(Product product);
}
