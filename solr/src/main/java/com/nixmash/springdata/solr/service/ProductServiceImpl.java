package com.nixmash.springdata.solr.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.repository.SolrProductRepository;
import com.nixmash.springdata.solr.repository.custom.DerivedSolrProductRepository;
import com.nixmash.springdata.solr.repository.derived.MyProductRepository;
import com.nixmash.springdata.solr.repository.factory.CustomProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Resource
	SolrProductRepository searchRepository;

	@Resource
	CustomProductRepository customProductRepository;

	@Resource
	DerivedSolrProductRepository derivedProductRepository;

	@Resource
	MyProductRepository myProductRepository;

	@Override
	public List<Product> displayAvailable() {
		logger.debug("Retrieving all available products");
		return searchRepository.findByAvailableTrue();
	}

	@Override
	public Iterable<Product> displayAllRecords() {
		logger.debug("Retrieving all records in index");
		return derivedProductRepository.findAll();
	}

	@Override
	public Iterable<Product> displayByNameOrCategory(String searchTerm) {
		logger.debug("Retrieving products by findByQueryAnnotation - ('name:*?0* OR cat:*?0*')");
		// return todoRepository.findByQueryAnnotation(searchTerm,
		// sortByIdDesc());
		return myProductRepository.findAll();
	}

	@Override
	public Product getProduct(String Id) {
		return customProductRepository.findOne(Id);
	}

	@Override
	public void updateProductName(Product product) {
		customProductRepository.update(product);
	}

	private Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, Product.ID_FIELD);
	}

}
