package com.nixmash.springdata.solr;

import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.repository.custom.CustomProductRepository;
import com.nixmash.springdata.solr.service.ProductService;

/**
 * 
 * NixMash Spring Notes: ---------------------------------------------------
 * 
 * Based on Christoph Strobl's Spring Solr Repository Example for Spring Boot
 * 
 * On GitHub: https://goo.gl/JoAYaT
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SolrApplicationTests extends SolrContext {

	private static final String SOLR_STRING = "solr";
	private static final int PRODUCT_ID = 1000;
	private static final int INITIAL_RECORD_COUNT = 55;
	private static final int TEST_RECORD_COUNT = 10;

	@Autowired
	SolrOperations solrOperations;

	// @Before
	// public void setUp() {
	//
	// }

	@After
	public void tearDown() {
		Query query = new SimpleQuery(new SimpleStringCriteria("cat:test"));
		solrOperations.delete(query);
		solrOperations.commit();
	}

	@Resource
	CustomProductRepository customProductRepository;

	@Autowired
	private ProductService productService;
	
	@Test
	public void badSimpleQueryThrowsUncategorizedSolrException() {
		int i = 0;
		try {
			productService.getProductsWithUserQuery("bad:field");
		} catch (Exception ex) {
			i++;
			Assert.assertTrue(ex instanceof UncategorizedSolrException);
		}
		try {
			productService.getProductsWithUserQuery("bad::format");
		} catch (UncategorizedSolrException ex) {
			i++;
			Assert.assertTrue(ex instanceof UncategorizedSolrException);
		}
		try {
			productService.getProductsWithUserQuery("name:goodQuery");
		} catch (UncategorizedSolrException ex) {
			i++;
		}
		Assert.assertEquals(2, i);
	}

	@Test
	public void testCustomQueries() {

		// Named Query from named-queries.properties
		List<Product> products = customProductRepository.findByNameOrCategory(SOLR_STRING, sortByIdDesc());
		Assert.assertEquals(1, products.size());

		// Method Name Query test for findByPopularityGreaterThanEqual()
		Product product = SolrTestUtils.createProduct(PRODUCT_ID);
		customProductRepository.save(product);

		Page<Product> popularProducts = customProductRepository.findByPopularityGreaterThanEqual(10000,
				new PageRequest(0, 10));
		Assert.assertEquals(1, popularProducts.getTotalElements());
		Assert.assertEquals(Integer.toString(PRODUCT_ID), popularProducts.getContent().get(0).getId());

	}

	@Test
	public void retrieveAllRecordCount() {
		Query query = new SimpleQuery(new SimpleStringCriteria("*:*"));
		Page<Product> products = solrOperations.queryForPage(query, Product.class);
		Assert.assertEquals(INITIAL_RECORD_COUNT, products.getTotalElements());
	}

	@Test
	public void simpleQueryTest() {
		List<Product> baseList = SolrTestUtils.createProductList(10);
		customProductRepository.save(baseList);
		Assert.assertEquals(baseList.size(), TEST_RECORD_COUNT);
		Assert.assertEquals(INITIAL_RECORD_COUNT + TEST_RECORD_COUNT, customProductRepository.count());

		List<Product> productsByCategory = customProductRepository.findProductsBySimpleQuery("cat:test");
		Assert.assertEquals(TEST_RECORD_COUNT, productsByCategory.size());
	}

	@Test
	public void testProductCRUD() {

		// create local product object
		Product product = SolrTestUtils.createProduct(PRODUCT_ID);

		// save product to Solr Index and confirm index count increased by 1
		customProductRepository.save(product);
		Assert.assertEquals(INITIAL_RECORD_COUNT + 1, customProductRepository.count());

		// find single product from Solr
		Product loaded = customProductRepository.findOne(Integer.toString(PRODUCT_ID));
		Assert.assertEquals(product.getName(), loaded.getName());

		// update product name in Solr and confirm index count not changed
		loaded.setName("changed named");
		customProductRepository.save(loaded);
		Assert.assertEquals(INITIAL_RECORD_COUNT + 1, customProductRepository.count());

		// retrieve product from Solr and confirm name change
		loaded = customProductRepository.findOne(Integer.toString(PRODUCT_ID));
		Assert.assertEquals("changed named", loaded.getName());

		// delete the test product in Solr and confirm index count equal to initial count
		customProductRepository.delete(loaded);
		Assert.assertEquals(INITIAL_RECORD_COUNT, customProductRepository.count());

	}

	private Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, Product.ID_FIELD);
	}

}
