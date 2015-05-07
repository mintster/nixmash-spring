package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.config.DevConfiguration;
import com.nixmash.springdata.jpa.model.ContactEntity;
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

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(classes = DevConfiguration.class)
@Transactional
@ActiveProfiles("dev")
public class ContactServiceTest {

    @Autowired
    private ContactService service;

    @Test
    public void findByFirstName() throws NotFoundException {
        ContactEntity contact = service.findById(1L);
        assertEquals(contact.getFirstName(), "Summer");
    }

}
