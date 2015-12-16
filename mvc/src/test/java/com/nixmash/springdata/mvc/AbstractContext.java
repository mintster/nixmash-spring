package com.nixmash.springdata.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.mvc.config.SecurityConfig;
import com.nixmash.springdata.mvc.config.WebConfig;
import com.nixmash.springdata.solr.enums.SolrConfigProfile;

@SuppressWarnings("deprecation")
@ContextConfiguration(classes = { WebConfig.class, ApplicationConfig.class, SecurityConfig.class })
@WebAppConfiguration
@Transactional
@ActiveProfiles({ DataConfigProfile.H2, SolrConfigProfile.DEV })
public class AbstractContext {

	@Autowired
	protected WebApplicationContext context;

}
