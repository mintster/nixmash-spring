package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.model.ContactPhone;
import com.nixmash.springdata.jpa.model.ContactTestUtil;
import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(classes = ApplicationConfig.class)
@Transactional
@ActiveProfiles(DataConfigProfile.H2)
public class ContactServiceTest {

    final String FIRST_NAME_CONTACT_ID_4L = "ROBIN";

    @Autowired
    private ContactService contactService;

    @Test
    public void findByFirstName() throws NotFoundException {
        Contact contact = contactService.findById(1L);
        assertEquals(contact.getFirstName(), "Summer");
    }

    @Test
    public void findByContactIdWithDetail() throws NotFoundException {
        Contact contact = contactService.getContactByIdWithDetail(1L);
        assertTrue(contact.getContactPhones().size() == 2);
    }

    @Test
    public void addContact() {

        // Retrieve all contacts to compare size before and after record addition
        List<Contact> contacts = contactService.findAll();
        int originalContactCount = contacts.size();

        ContactDTO contactDTO = ContactTestUtil.newContactDTO();
        Contact contact = contactService.add(contactDTO);
        assertThat(contact.getContactId(), is(11L));

        // We entered a ContactDTO with 2 Phone Records - Database Count
        List<ContactPhone> contactPhones =
                contactService.findContactPhonesByContactId(contact.getContactId());
        int phoneCount = contactPhones.size();
        assertEquals(phoneCount, 2);

        // Confirm Contact contains the new phone records
        contact = contactService.findById(contact.getContactId());
        phoneCount = contact.getContactPhones().size();
        assertEquals(phoneCount, 2);

        // Confirm new Contact is retrieved when viewing all contacts
        contacts = contactService.findAll();
        int finalContactCount = contacts.size();
        assertThat(finalContactCount, is(greaterThan(originalContactCount)));
    }

    @Test
    public void deleteContact() throws com.nixmash.springdata.jpa.service.NotFoundException {
        List<Contact> contacts = contactService.findAll();
        int originalContactCount = contacts.size();

        Contact contact = contactService.deleteById(3L);
        contacts = contactService.findAll();

        int finalContactCount = contacts.size();
        assertThat(finalContactCount, is(lessThan(originalContactCount)));

        // Confirm no contact phones in database for deleted contact
        List<ContactPhone> contactPhones = contactService.findContactPhonesByContactId(3L);
        int phoneCount = contactPhones.size();
        assertEquals(phoneCount, 0);

    }

    @Test
    public void updateContact() throws
            com.nixmash.springdata.jpa.service.NotFoundException {

        // Contact with ID=4 in H2Database Robin Sullivan, 2 Phones
        Contact contact =  contactService.findById(4L);
        ContactDTO contactDTO = ContactTestUtil.contactToContactDTO(contact);
        assertEquals(contactDTO.getFirstName().toUpperCase(), FIRST_NAME_CONTACT_ID_4L);

        // Contact's first phone: Mobile "1-234-628-6511" -> "1-234-628-9999"
        assertThat(getFirstContactPhone(contactDTO), endsWith("6511"));

        // Contact's last name: Sullivan -> Sullivananny
        contactDTO.setLastName("Sullivananny");

        // Update Contact's first phone number
        contactDTO
                .getContactPhones()
                .stream()
                .filter(t -> t.getPhoneType().toUpperCase().equals("MOBILE"))
                .findFirst()
                .get()
                .setPhoneNumber("1-234-628-9999");

        contactService.update(contactDTO);

        // reload Contact from Repository to confirm records are updated
        contact =  contactService.findById(4L);
        contactDTO = ContactTestUtil.contactToContactDTO(contact);
        assertThat(contactDTO.getLastName(), endsWith("nanny"));
        assertThat(getFirstContactPhone(contactDTO), endsWith("9999"));
    }

    private String getFirstContactPhone(ContactDTO contactDTO) {
        return contactDTO
                .getContactPhones()
                .stream()
                .filter(t -> t.getPhoneType().toUpperCase().equals("MOBILE"))
                .findFirst()
                .get()
                .getPhoneNumber();
    }

}