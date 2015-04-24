package com.nixmash.springdata;

import com.nixmash.springdata.config.SpringHbnConfiguration;
import com.nixmash.springdata.config.SpringJpaConfiguration;
import com.nixmash.springdata.config.SpringProperties;
import com.nixmash.springdata.dev.SpringDevelopment;
import com.nixmash.springdata.hbn.service.ContactService;
import com.nixmash.springdata.jpa.service.ContactJpaService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(SpringHbnConfiguration.class);
        ctx.register(SpringJpaConfiguration.class);
        ctx.refresh();

        ContactService contactService = (ContactService) ctx.getBean("hbnContactService");
        ContactJpaService contactJpaService = (ContactJpaService) ctx.getBean("jpaContactService");
        SpringProperties springProperties = ctx.getBean(SpringProperties.class);

        SpringDevelopment springDevelopment = new
                SpringDevelopment(springProperties, contactJpaService, contactService);

        springDevelopment.propertiesDemo();
        springDevelopment.hibernateDemo();
        springDevelopment.jpaDemo();

    }

}
