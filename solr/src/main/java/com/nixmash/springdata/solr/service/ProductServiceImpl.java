package com.nixmash.springdata.solr.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Resource
	private ProductRepository repository;

	@Override
	public List<Product> search(String searchTerm) {
		logger.debug("Searching documents with search term: {}", searchTerm);
		return repository.search(searchTerm);
	}

}
