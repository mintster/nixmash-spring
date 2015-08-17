package com.nixmash.springdata.mvc;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.mvc.config.SecurityConfig;
import com.nixmash.springdata.mvc.config.WebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(classes = {WebConfig.class,
		ApplicationConfig.class, SecurityConfig.class})
@WebAppConfiguration
@Transactional
@ActiveProfiles(DataConfigProfile.H2)
public class AbstractContext {

	@Autowired
	protected WebApplicationContext context;

}


