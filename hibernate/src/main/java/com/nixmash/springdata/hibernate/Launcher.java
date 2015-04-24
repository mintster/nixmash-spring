package com.nixmash.springdata.hibernate;

import com.nixmash.springdata.hibernate.config.SpringHbnConfiguration;
import com.nixmash.springdata.hibernate.config.SpringProperties;
import com.nixmash.springdata.hibernate.dev.SpringDevelopment;
import com.nixmash.springdata.hibernate.service.ContactService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(SpringHbnConfiguration.class);
        ctx.refresh();

        ContactService contactService = (ContactService) ctx.getBean("hbnContactService");
        SpringProperties springProperties = ctx.getBean(SpringProperties.class);

        SpringDevelopment springDevelopment = new
                SpringDevelopment(springProperties, contactService);

        springDevelopment.propertiesDemo();
        springDevelopment.hibernateDemo();

    }

}
