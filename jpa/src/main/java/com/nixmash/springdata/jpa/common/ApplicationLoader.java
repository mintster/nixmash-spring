package com.nixmash.springdata.jpa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLoader implements CommandLineRunner {

    // Simple demonstration of using CommandLineRunner Interface
    private static final Logger logger = LoggerFactory.getLogger(ApplicationLoader.class);

    @Autowired
    Environment environment;

    @Override
    public void run(String... strings) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String option : strings) {
            sb.append(" ").append(option);
        }
        sb = sb.length() == 0 ? sb.append("No Options Specified") : sb;
        String activeProfile = environment.getActiveProfiles()[0];

        logger.info(String.format("WAR launched with following profiles: %s", sb.toString()));
        logger.info(String.format("Current JPA Active Profile: %s", activeProfile));
    }
}
