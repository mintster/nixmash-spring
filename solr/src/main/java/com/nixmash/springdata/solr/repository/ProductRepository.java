package com.nixmash.springdata.solr.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.nixmash.springdata.solr.model.Product;

public interface ProductRepository extends CrudRepository<Product, String> {

	List<Product> findByAvailableTrue();
}
