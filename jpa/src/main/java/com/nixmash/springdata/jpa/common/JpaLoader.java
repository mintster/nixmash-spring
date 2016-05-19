package com.nixmash.springdata.jpa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JpaLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(JpaLoader.class);

    @Autowired
    Environment environment;

    @Override
    public void run(String... args) throws Exception {

        String activeProfile = environment.getActiveProfiles()[0];
        logger.info(String.format("Current JPA Active Profile: %s", activeProfile));

        String applicationVersion = environment.getProperty("nixmash.spring.jpa.version");
        logger.info(String.format("NixMash Spring JPA Application Version: %s", applicationVersion));

    }
}
