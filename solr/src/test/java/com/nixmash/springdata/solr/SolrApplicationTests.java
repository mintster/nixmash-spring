package com.nixmash.springdata.solr;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nixmash.springdata.solr.config.SolrApplicationConfig;
import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SolrApplicationConfig.class)
@ActiveProfiles("dev")
public class SolrApplicationTests {

	private static final String PROPERTY_NAME_SOLR_HOME = "solr.solr.home";

	@Autowired
	Environment environment;

	@Autowired
	ProductService productService;

	@Test
	public void contextLoads() {
		assertNotNull(environment.getRequiredProperty(PROPERTY_NAME_SOLR_HOME));
	}

	@Test
	public void testProductService() {
		Iterable<Product> pages = productService.displayAvailable();
		// Assert.assertEquals(1, list.size());
	}

}
