package com.nixmash.springdata.dev;

import com.nixmash.springdata.config.SpringProperties;
import com.nixmash.springdata.config.SpringUtils;
import com.nixmash.springdata.hbn.service.ContactService;
import com.nixmash.springdata.jpa.service.ContactJpaService;
import com.nixmash.springdata.model.Contact;
import com.nixmash.springdata.model.ContactTelDetail;

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
    ContactService contactService;

    public SpringDevelopment(SpringProperties springProperties,
                             ContactJpaService contactJpaService,
                             ContactService contactService)
    {
        this.springProperties = springProperties;
        this.contactJpaService = contactJpaService;
        this.contactService = contactService;
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
        contactService.updateContact(contact);
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

    // region Hibernate Demos

    public void hibernateDemo() {

        Contact contact = contactService.getContact(1l);
        SpringUtils.listContact("HIBERNATE CONTACT(1L)", contact);

        List<Contact> contacts = contactService.getContacts();
        SpringUtils.listContacts("HIBERNATE CONTACTS", contacts);

        contacts = contactService.getContactsWithDetail();
        listHbnContactsWithDetail(contacts);

    }

    private void listHbnContactsWithDetail(List<Contact> contacts) {
        System.out.println("LISTING CONTACTS WITH DETAILS ---------------------------------");
        System.out.println();
        for (Contact contact : contacts) {
            System.out.println(contact);
            if (contact.getContactTelDetails() != null) {
                contact.getContactTelDetails().forEach(System.out::println);
            }
            if (contact.getHobbies() != null) {
                contact.getHobbies().forEach(System.out::println);
            }
            System.out.println();
        }
    }

    // endregion

}
