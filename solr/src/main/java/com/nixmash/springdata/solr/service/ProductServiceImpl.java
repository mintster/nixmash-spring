package com.nixmash.springdata.solr.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.repository.SolrProductRepository;
import com.nixmash.springdata.solr.repository.factory.CustomProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Resource
	SolrProductRepository searchRepository;

	@Resource
	CustomProductRepository customProductRepository;

	@Override
	public List<Product> displayAvailable() {
		logger.debug("Retrieving all available products");
		return searchRepository.findByAvailableTrue();
	}

	@Override
	public Iterable<Product> displayAllProducts() {
		logger.debug("Retrieving all products");
		return searchRepository.findAll();
	}

}
