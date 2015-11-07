package com.nixmash.springdata.solr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.repository.SolrProductRepository;
import com.nixmash.springdata.solr.repository.factory.CustomProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	SolrProductRepository solrProductRepository;

	@Autowired
	CustomProductRepository customProductRepository;

	@Override
	public Page<Product> displayAvailable() {
		logger.debug("Retrieving all available products");
		return solrProductRepository.findByAvailableTrue();
	}

	@Override
	public Iterable<Product> displayAllProducts() {
		logger.debug("Retrieving all products");
		return solrProductRepository.findAll();
	}

}
