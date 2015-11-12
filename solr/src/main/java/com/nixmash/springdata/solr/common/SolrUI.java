package com.nixmash.springdata.solr.common;

import javax.annotation.Resource;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.service.ProductService;

@Component
public class SolrUI {

	private static final String PROPERTY_NAME_PROFILE_DESCRIPTION = "profile.description";

	@Resource
	private Environment environment;

	@Resource
	private ProductService service;

	public void init() {
		// propertiesDemo();
		// productListDemo();
		// productUpdateDemo();
		productAnnotationSearchDemo();
	}

	// region Demos

	private void productAnnotationSearchDemo() {
		// Iterable<Product> products =
		// service.displayByNameOrCategory("electronics");
		Iterable<Product> products = service.displayAvailable();
		for (Product product : products) {
			System.out.println(product.getName());
		}
	}

	private void productUpdateDemo() {
		Product product = service.getProduct("SOLR1000");
		product.setName("Solr, The Enterprise Http Search Server");
		service.updateProductName(product);
	}

	private void propertiesDemo() {
		System.out.println(environment.getProperty(PROPERTY_NAME_PROFILE_DESCRIPTION));
	}

	private void productListDemo() {

		Iterable<Product> products = service.displayAllRecords();
		for (Product product : products) {
			System.out.println("Product: " + product.getName());
		}
	}
	// endregion

}
