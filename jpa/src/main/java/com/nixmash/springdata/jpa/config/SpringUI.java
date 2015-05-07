package com.nixmash.springdata.jpa.config;

import com.nixmash.springdata.jpa.model.ContactEntity;
import com.nixmash.springdata.jpa.model.ContactTelDetailEntity;
import com.nixmash.springdata.jpa.service.ContactService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 4/22/15
 * Time: 3:32 PM
 */
public class SpringUI {

    // region Properties Demo

    SpringProperties springProperties;
    ContactService contactService;

    public SpringUI(SpringProperties springProperties, ContactService contactEntityService)
    {
        this.springProperties = springProperties;
        this.contactService = contactEntityService;
    }

    public void propertiesDemo() {
        SpringUtils.printProperty(
                "springProperties.getToken()",
                springProperties.getToken());
    }

    // endregion

    // region Add Contact

    public void AddContact() {
        ContactEntity contact = new ContactEntity();
        contact.setFirstName("Michael");
        contact.setLastName("Jackson");
        java.util.Date utilDate = new java.util.Date();
        contact.setBirthDate(new java.sql.Date(utilDate.getTime()));
        ContactTelDetailEntity contactTelDetail =
                new ContactTelDetailEntity("Home", "1111111111");
        contact.addContactTelDetailEntity(contactTelDetail);
        contactTelDetail = new ContactTelDetailEntity("Mobile", "2222222222");
        contact.addContactTelDetailEntity(contactTelDetail);
//        contactService.save(contact);
    }

    // endregion

    // region Spring Data JPA Demos


    public void entityDemo() {
        SpringUtils.listContactEntities("ENTITIES FIND ALL",
                contactService.findAll());
        SpringUtils.listContactEntities("ENTITIES FIND BY FIRST NAME",
                contactService.findByFirstName("Barry"));
        SpringUtils.listContactEntities("ENTITIES FIND BY FIRST AND LAST NAME",
                contactService.findByFirstNameAndLastName("Tad", "Grant"));

        List<ContactEntity> contacts = contactService.getContactsWithDetail();
        SpringUtils.listContactEntitiesWithDetail(contacts);

    }


    public void showContact(Long ID) {
        ContactEntity contact = contactService.findById(ID);
        System.out.println(contact.getFirstName());
    }
    // endregion


}
