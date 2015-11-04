package com.nixmash.springdata.solr.common;

import java.util.List;

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
		propertiesDemo();
		productListDemo();
	}

	// region Properties Demo

	public void propertiesDemo() {
		System.out.println(environment.getProperty(PROPERTY_NAME_PROFILE_DESCRIPTION));
	}

	public void productListDemo() {
		List<Product> products = service.search("*:*");
		for (Product product : products) {
			System.out.println("Product: " + product.getName());
		}
	}
	// endregion

}
