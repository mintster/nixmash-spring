package com.nixmash.springdata.jsoup.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JsoupLoader implements CommandLineRunner{

    private static final Logger logger = LoggerFactory.getLogger(JsoupLoader.class);

    @Autowired
    Environment environment;

    @Override
    public void run(String... args) throws Exception {
        String applicationVersion = environment.getProperty("nixmash.spring.jsoup.version");
        logger.info(String.format("NixMash Spring Jsoup Application Version: %s", applicationVersion));
    }
}
