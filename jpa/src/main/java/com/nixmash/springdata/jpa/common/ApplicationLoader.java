package com.nixmash.springdata.jpa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLoader implements CommandLineRunner {

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
        
        PropertySource<?> ps = new SimpleCommandLinePropertySource(strings);
        String appUrl = (String) ps.getProperty("appurl");
        
        logger.info(String.format("Command-line appurl is %s", appUrl));
        
        String applicationPropertyUrl = environment.getProperty("spring.social.application.url");
        logger.info(String.format("Current Spring Social ApplicationUrl is %s", applicationPropertyUrl));
        
    }
}
