package com.nixmash.springdata.solr;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nixmash.springdata.solr.config.SolrApplicationConfig;
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
@ContextConfiguration(classes = SolrApplicationConfig.class)
@Transactional
@ActiveProfiles("dev")
public class SolrContext {

	private static final String PROPERTY_NAME_SOLR_HOME = "solr.solr.home";

	@Autowired
	ProductService productService;

	@Autowired
	Environment environment;

	@Test
	public void contextLoads() {
		assertNotNull(environment.getRequiredProperty(PROPERTY_NAME_SOLR_HOME));
	}

}
