package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.model.Contact;
import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(classes = ApplicationConfig.class)
@Transactional
@ActiveProfiles(DataConfigProfile.H2)
public class ContactServiceTest {

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

}
