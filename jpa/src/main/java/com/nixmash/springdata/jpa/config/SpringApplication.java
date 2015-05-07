package com.nixmash.springdata.jpa.config;

import com.nixmash.springdata.jpa.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 5/5/15
 * Time: 11:26 AM
 */
@Component
public class SpringApplication {

    @Autowired
    private ContactService contactService;

    @Autowired
    private SpringProperties springProperties;

    public void init()
    {
        SpringUI ui = new SpringUI(springProperties, contactService);
        ui.propertiesDemo();
        ui.entityDemo();
    }
}
