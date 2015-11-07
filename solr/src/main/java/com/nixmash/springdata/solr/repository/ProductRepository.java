package com.nixmash.springdata.solr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;

import com.nixmash.springdata.solr.model.Product;

public interface ProductRepository extends CrudRepository<Product, String> {

	Page<Product> findByAvailableTrue();
}
