package com.nixmash.springdata.mvc;

import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.solr.enums.SolrConfigProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = Application.class)
@Transactional
@ActiveProfiles({ DataConfigProfile.H2, SolrConfigProfile.DEV })
public class AbstractContext {

	@Autowired
	protected WebApplicationContext context;

}
