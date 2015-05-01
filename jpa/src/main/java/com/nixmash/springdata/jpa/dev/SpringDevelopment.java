package com.nixmash.springdata.jpa.dev;

import com.nixmash.springdata.jpa.config.SpringProperties;
import com.nixmash.springdata.jpa.config.SpringUtils;
import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.model.ContactEntity;
import com.nixmash.springdata.jpa.model.ContactTelDetail;
import com.nixmash.springdata.jpa.service.ContactEntityService;
import com.nixmash.springdata.jpa.service.ContactJpaService;

import java.util.Date;
import java.util.List;

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
    ContactEntityService contactEntityService;

    public SpringDevelopment(SpringProperties springProperties,
                             ContactJpaService contactJpaService, ContactEntityService contactEntityService)
    {
        this.springProperties = springProperties;
        this.contactJpaService = contactJpaService;
        this.contactEntityService = contactEntityService;
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

        List<Contact> contacts = contactJpaService.getContactsWithDetail();
        SpringUtils.listContactsWithDetail(contacts);
    }

    public void entityDemo() {
        SpringUtils.listContactEntities("ENTITIES FIND ALL",
                contactEntityService.findAll());
        SpringUtils.listContactEntities("ENTITIES FIND BY FIRST NAME",
                contactEntityService.findByFirstName("Barry"));
        SpringUtils.listContactEntities("ENTITIES FIND BY FIRST AND LAST NAME",
                contactEntityService.findByFirstNameAndLastName("Tad", "Grant"));

        List<ContactEntity> contacts = contactEntityService.getContactsWithDetail();
        SpringUtils.listContactEntitiesWithDetail(contacts);

    }


    // endregion


}
