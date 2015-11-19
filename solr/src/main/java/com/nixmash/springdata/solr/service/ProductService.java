package com.nixmash.springdata.solr.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.nixmash.springdata.solr.model.Product;

public interface ProductService {

	public List<Product> getAvailableProducts();

	Iterable<Product> getAllRecords();

	List<Product> getProducts();

	List<Product> getProductsByQuery();

	Iterable<Product> displayByNameOrCategory(String searchTerm);

	Product getProduct(String Id);

	void updateProductName(Product product);

	Iterable<Product> displayByNamedQuery(String searchTerm);

	Page<Product> getProductsByPopularity(int popularity);

	Page<Product> getTestRecords();
}
