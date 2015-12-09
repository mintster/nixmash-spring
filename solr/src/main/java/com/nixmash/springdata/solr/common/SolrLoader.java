package com.nixmash.springdata.solr.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SolrLoader implements CommandLineRunner {

    // Simple demonstration of using CommandLineRunner Interface
    private static final Logger logger = LoggerFactory.getLogger(SolrLoader.class);

    @Autowired
    Environment environment;

    @Override
    public void run(String... strings) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String option : strings) {
            sb.append(" ").append(option);
        }
        sb = sb.length() == 0 ? sb.append("No Options Specified") : sb;
        
        try {
			String activeProfile = environment.getActiveProfiles()[1];
			logger.info(String.format("Current Solr Active Profile: %s", activeProfile));
		} catch (Exception e) {
			logger.info("No second profile entered.");
		}
    }
}
