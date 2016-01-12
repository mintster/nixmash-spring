package com.nixmash.springdata.solr.common;

import java.text.MessageFormat;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Component;

import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.service.ProductService;

@Component
public class SolrUI {

	private static final String SOLR_RECORD_ID = "SOLR1000";

	@Resource
	private Environment environment;

	@Resource
	private ProductService service;

	@Autowired
	private SolrSettings solrSettings;

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
		ANNOTATED_QUERY,
		FACET_ON_AVAILABLE,
		FACET_ON_CATEGORY,
		FACET_ON_NAME,
		SIMPLE_QUERY
	};

	// @formatter:on

	public void init() {
		DEMO demo = DEMO.ALL_PRODUCTS;

		String[] profiles = environment.getActiveProfiles();
		if (profiles[0].equals("dev"))
			System.out.println("DEVELOPMENT mode: Embedded SOLR Home: " + solrSettings.getSolrEmbeddedPath());
		else
			System.out.println("PRODUCTION mode: SOLR Server Url: " + solrSettings.getSolrServerUrl());
		System.out.println("Running Demo: " + demo.name() + "\n");

		runDemos(demo);
	}

	private void runDemos(DEMO demo) {

		switch (demo) {

		case SIMPLE_QUERY:
			// List<Product> usqProducts = service.getProductsWithUserQuery("name:memory AND name:corsair) AND
			// popularity:[6 TO *]");
			// List<Product> usqProducts = service.getProductsWithUserQuery("name:Western+Digital AND inStock:TRUE");
			// List<Product> usqProducts = service.getProductsWithUserQuery("cat:memory");
			// List<Product> usqProducts = service.getProductsWithUserQuery("features::printer");
			List<Product> usqProducts = service.getProductsWithUserQuery("inStock:true");
			printProducts(usqProducts);
			break;

		case FACET_ON_NAME:

			FacetPage<Product> fnfacetPage = service.autocompleteNameFragment("pr", new PageRequest(0, 1));
			Page<FacetFieldEntry> fnPage = fnfacetPage.getFacetResultPage(Product.NAME_FIELD);

			for (FacetFieldEntry entry : fnPage) {
				System.out.println(String.format("%s:%s \t %s", entry.getField().getName(), entry.getValue(),
						entry.getValueCount()));
			}

			break;

		case FACET_ON_AVAILABLE:

			FacetPage<Product> avfacetPage = service.getFacetedProductsAvailable();
			Page<FacetFieldEntry> avPage = avfacetPage.getFacetResultPage(Product.AVAILABLE_FIELD);

			for (FacetFieldEntry entry : avPage) {
				System.out.println(String.format("%s:%s \t %s", entry.getField().getName(), entry.getValue(),
						entry.getValueCount()));
			}

			break;

		case FACET_ON_CATEGORY:

			FacetPage<Product> catfacetPage = service.getFacetedProductsCategory();
			Page<FacetFieldEntry> catPage = catfacetPage.getFacetResultPage(Product.CATEGORY_FIELD);

			for (FacetFieldEntry entry : catPage) {
				System.out.println(String.format("%s:%s \t %s", entry.getField().getName(), entry.getValue(),
						entry.getValueCount()));
			}

			break;

		case METHOD_NAME_QUERY:

			List<Product> mnqProducts = service.getProductsByStartOfName("power cord");
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

			List<Product> daProducts = service.getProductsByFilter();
			printProducts(daProducts);

			List<Product> qProducts = service.getProducts();
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
		System.out.println("");
		for (Product product : products) {
			MessageFormat mf = new MessageFormat("{0} | Popularity: {1} | Lng/Lat: {2},{3}");
			Object[] items = { 
					product.getName(), 
					product.getPopularity(), 
					product.getPoint().getX(),
					product.getPoint().getY() };

			if (product.getPoint().getX() < 0) {
				mf = new MessageFormat("{0} | Popularity: {1}");
			}

			System.out.println(mf.format(items));
			i++;
		}
		System.out.println("\nTOTAL RECORDS: " + i);
	}

}
