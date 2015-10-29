package com.nixmash.springdata.solr.service;

import java.util.List;

import com.nixmash.springdata.solr.model.Product;

public interface ProductService {

	public List<Product> search(String searchTerm);
}
