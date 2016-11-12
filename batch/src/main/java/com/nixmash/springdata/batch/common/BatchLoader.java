package com.nixmash.springdata.batch.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class BatchLoader implements CommandLineRunner{

    private static final Logger logger = LoggerFactory.getLogger(BatchLoader.class);

    @Autowired
    Environment environment;

    @Override
    public void run(String... args) throws Exception {
        String applicationVersion = environment.getProperty("nixmash.spring.batch.version");
        logger.info(String.format("NixMash Spring Batch Application Version: %s", applicationVersion));
    }
}
