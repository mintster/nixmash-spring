package com.nixmash.springdata.solr.common;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.service.ProductService;

@Component
public class SolrUI {

	private static final String PROPERTY_NAME_PROFILE_DESCRIPTION = "profile.description";
	private static final String SOLR_RECORD_ID = "SOLR1000";

	@Resource
	private Environment environment;

	@Resource
	private ProductService service;

	// @formatter:off
	
	private enum DEMO {
		ALL_PRODUCTS, 
		NAMED_QUERY, 
		UPDATE_RECORD, 
		METHOD_NAME_QUERY, 
		AVAILABLE_PRODUCTS, 
		ALL_RECORDS,
		TEST_RECORDS,
		CRITERIA_SEARCH,
		ANNOTATED_QUERY
		
	};

	// @formatter:on

	public void init() {
		DEMO demo = DEMO.METHOD_NAME_QUERY;
		System.out.println(environment.getProperty(PROPERTY_NAME_PROFILE_DESCRIPTION));
		System.out.println("Running Demo: " + demo.name() + "\n");

		runDemos(demo);
	}

	private void runDemos(DEMO demo) {

		switch (demo) {

		case METHOD_NAME_QUERY:
			
			List<Product> mnqProducts = service.getProductsByStartOfName("co");
			printProducts(mnqProducts);
			break;

		case ANNOTATED_QUERY:

			List<Product> aqProducts = service.getProductsByNameOrCategoryAnnotatedQuery("canon");
			printProducts(aqProducts);
			break;

		case NAMED_QUERY:

			Iterable<Product> nqProducts = service.getProductsByNameOrCategory("canon");
			printProducts(nqProducts);
			break;


		case TEST_RECORDS:

			Page<Product> testProducts = service.getTestRecords();
			printProducts(testProducts);
			break;

		case AVAILABLE_PRODUCTS:
			List<Product> avpProducts = service.getAvailableProducts();
			printProducts(avpProducts);
			break;

		case ALL_PRODUCTS:

			List<Product> daProducts = service.getProducts();
			printProducts(daProducts);

			List<Product> qProducts = service.getProductsByQuery();
			printProducts(qProducts);

			break;

		case ALL_RECORDS:

			Iterable<Product> allRecords = service.getAllRecords();
			printProducts(allRecords);
			break;

		case UPDATE_RECORD:

			Product urProduct = service.getProduct(SOLR_RECORD_ID);
			System.out.println(String.format("Original Product Name: %s", urProduct.getName()));
			urProduct.setName("Solr, The Enterprisey Http Search Server");
			service.updateProductName(urProduct);

			Product urProductUpdated = service.getProduct(SOLR_RECORD_ID);
			System.out.println(String.format("New Product Name: %s", urProductUpdated.getName()));

			urProductUpdated.setName("Solr, The Enterprise Http Search Server");
			service.updateProductName(urProductUpdated);

			break;

		case CRITERIA_SEARCH:

			List<Product> csProducts = service.searchWithCriteria("Canon Camera memory");
			printProducts(csProducts);

			break;

		default:
			break;
		}

	}

	private void printProducts(Iterable<? extends Product> products) {
		int i = 0;
		for (Product product : products) {
			System.out.println(product.getName());
			i++;
		}
		System.out.println("\nTOTAL RECORDS: " + i);
	}

}
