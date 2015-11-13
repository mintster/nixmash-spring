package com.nixmash.springdata.solr.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.repository.custom.CustomProductRepository;
import com.nixmash.springdata.solr.repository.derived.DerivedProductRepository;
import com.nixmash.springdata.solr.repository.factory.FactoryProductRepository;
import com.nixmash.springdata.solr.repository.simple.SimpleProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Resource
	SimpleProductRepository simpleProductRepository;

	@Resource
	FactoryProductRepository factoryProductRepository;

	@Resource
	DerivedProductRepository derivedProductRepository;

	@Resource
	CustomProductRepository customProductRepository;

	@Override
	public List<Product> displayAvailable() {
		logger.debug("Retrieving all available products");
		return simpleProductRepository.findByAvailableTrue();
	}

	@Override
	public Iterable<Product> displayAllRecords() {
		logger.debug("Retrieving all records in index");
		return derivedProductRepository.findAll();
	}

	@Override
	public Iterable<Product> displayByNamedQuery(String searchTerm) {
		logger.debug("Retrieving products by namedQuery - ('name:*?0* OR cat:*?0*')");
		return customProductRepository.findByNamedQuery(searchTerm, sortByIdDesc());
	}

	@Override
	public Iterable<Product> displayByNameOrCategory(String searchTerm) {
		logger.debug("Retrieving products by findByQueryAnnotation - ('name:*?0* OR cat:*?0*')");
		// return todoRepository.findByQueryAnnotation(searchTerm,
		// sortByIdDesc());
		return customProductRepository.findAll();
	}

	@Override
	public Product getProduct(String Id) {
		return factoryProductRepository.findOne(Id);
	}

	@Override
	public void updateProductName(Product product) {
		factoryProductRepository.update(product);
	}

	private Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, Product.ID_FIELD);
	}

}
