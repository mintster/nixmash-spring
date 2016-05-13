package com.nixmash.springdata.jpa.components;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.common.ContactUtils;
import com.nixmash.springdata.jpa.common.SiteOptions;
import com.nixmash.springdata.jpa.dto.SiteOptionDTO;
import com.nixmash.springdata.jpa.exceptions.SiteOptionNotFoundException;
import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.service.ContactService;
import com.nixmash.springdata.jpa.service.SiteService;
import com.nixmash.springdata.jpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class ContactUI {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private ApplicationSettings applicationSettings;

    @Autowired
    DefaultListableBeanFactory beanFactory;

    @Autowired
    private SiteOptions siteOptions;

    public void init() {
//		propertiesDemo();
//		entityDemo();
//		randomDemo();
        beanScopeDemo();
    }

    private void beanScopeDemo() {
        System.out.println("Initialized SiteOptions Bean Property: " +
                siteOptions.getGoogleAnalyticsTrackingId());

        SiteOptionDTO siteOptionDTO = new SiteOptionDTO("siteName", "Updated Site Name");
        try {
            siteService.update(siteOptionDTO);
            siteService.update(new SiteOptionDTO("integerProperty", "8"));
        } catch (SiteOptionNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("New SiteOptions values: " + siteOptions.getSiteName() + " -- " + siteOptions.getIntegerProperty());
    }

    public void randomDemo() {
        System.out.println(ContactUtils.randomNegativeId());
        System.out.println(ContactUtils.randomNegativeId());
        System.out.println(ContactUtils.randomNegativeId());
        System.out.println(ContactUtils.randomNegativeId());
        System.out.println(ContactUtils.randomNegativeId());
        System.out.println(ContactUtils.randomNegativeId());
        System.out.println(ContactUtils.randomNegativeId());
        System.out.println(ContactUtils.randomNegativeId());
    }

    public void propertiesDemo() {
        ContactUtils.printProperty("ApplicationSettings.getIsDemoSite()",
                applicationSettings.getIsDemoSite().toString());
    }

    public void AddContact() {

        Contact contact = new Contact();

        // Contact contact = new Contact();
        // contact.setFirstName("Michael");
        // contact.setLastName("Jackson");
        // java.util.Date utilDate = new java.util.Date();
        // contact.setBirthDate(new java.sql.Date(utilDate.getTime()));
        // ContactPhone contactPhone =
        // new ContactPhone("Home", "1111111111");
        // contact.addContactPhone(contactPhone);
        // contactPhone = new ContactPhone("Mobile", "2222222222");
        // contact.addContactPhone(contactPhone);
        // contactService.save(contact);
    }

    public void entityDemo() {

//		UserConnection userConnection = userService.getUserConnectionByUserId("daver");
//		ContactUtils.listUserConnection("My User Connection", userConnection);
        ContactUtils.listUsersWithDetail(userService.getUsersByAuthorityId(1L));
        // SpringUtils.listUser("USER BY EMAIL",
        // userService.getByEmail("user@aol.com").get());
        // try {
        // SpringUtils.listContact("CONTACT BY EMAIL",
        // contactService.findContactById(1L));
        // } catch (ContactNotFoundException e) {
        // e.printStackTrace();
        // }

//		 ContactUtils.listContacts("ENTITIES FIND ALL", contactService.findAll());
        // SpringUtils.listContacts("ENTITIES FIND BY FIRST NAME",
        // contactService.findByFirstName("Barry"));
        // SpringUtils.listContacts("ENTITIES FIND BY FIRST AND LAST NAME",
        // contactService.findByFirstNameAndLastName("Tad", "Grant"));
        //
        // SpringUtils.listContact("SINGLE CONTACT: ",
        // contactService.getContactByEmail("Nam.nulla@pedenonummyut.edu"));
        // SpringUtils.listContactsWithDetail(contactService.getContactsWithDetail());
        //
        // SpringUtils.listContactWithDetail(contactService.getContactByIdWithDetail(2L));
        //
        // SpringUtils.listContacts("FIND BY FIRST NAME",
        // contactService.findByFirstName("Summer"));
        //
        // SpringUtils.listContactWithDetail(contactService.getContactByIdWithDetail(1L));

        // SpringUtils.contactToContactDTO(contactService.getContactByIdWithDetail(2L));

        // try {
        // contactService.update(SpringUtils.contactToContactDTO(contactService.getContactByIdWithDetail(2L)));
        // } catch (NotFoundException e) {
        // e.printStackTrace();
        // }
    }

}
