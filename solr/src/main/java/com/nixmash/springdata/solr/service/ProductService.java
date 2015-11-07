package com.nixmash.springdata.solr.service;

import com.nixmash.springdata.solr.model.Product;

public interface ProductService {

	public Iterable<Product> displayAvailable();

	Iterable<Product> displayAllProducts();
}
