package com.nixmash.springdata.jpa.dev;

import com.nixmash.springdata.jpa.config.SpringProperties;
import com.nixmash.springdata.jpa.config.SpringUtils;
import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.model.ContactTelDetail;
import com.nixmash.springdata.jpa.service.ContactJpaService;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 4/22/15
 * Time: 3:32 PM
 */
public class SpringDevelopment {

    // region Properties Demo

    SpringProperties springProperties;
    ContactJpaService contactJpaService;

    public SpringDevelopment(SpringProperties springProperties,
                             ContactJpaService contactJpaService)
    {
        this.springProperties = springProperties;
        this.contactJpaService = contactJpaService;
    }

    public void propertiesDemo() {
        SpringUtils.printProperty(
                "springProperties.getToken()",
                springProperties.getToken());
    }

    // endregion

    // region Add Contact

    public void AddContact() {
        Contact contact = new Contact();
        contact.setFirstName("Michael");
        contact.setLastName("Jackson");
        contact.setBirthDate(new Date());
        ContactTelDetail contactTelDetail =
                new ContactTelDetail("Home", "1111111111");
        contact.addContactTelDetail(contactTelDetail);
        contactTelDetail = new ContactTelDetail("Mobile", "2222222222");
        contact.addContactTelDetail(contactTelDetail);
//        contactJpaService.save(contact);
    }

    // endregion

    // region Spring Data JPA Demos

    public void jpaDemo() {
        SpringUtils.listContacts("JPA FIND ALL",
                contactJpaService.findAll());
        SpringUtils.listContacts("JPA FIND BY FIRST NAME",
                contactJpaService.findByFirstName("Barry"));
        SpringUtils.listContacts("JPA FIND BY FIRST AND LAST NAME",
                contactJpaService.findByFirstNameAndLastName("Tad", "Grant"));
    }

    // endregion


}
