package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.dto.AccessDTO;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Transactional
@ActiveProfiles(DataConfigProfile.H2)
public class AccessServiceTests {

    @Autowired
    private AccessService accessService;

    @Test
    public void emailApprovalTests() {

        assertTrue(accessService.isEmailApproved("guy@some.where.good.com"));
        assertTrue(accessService.isEmailApproved("guy@gmail.com"));

        assertFalse(accessService.isEmailApproved("guy@bademail"));
        assertFalse(accessService.isEmailApproved("guy@printemailtext.com"));
        assertFalse(accessService.isEmailApproved("guy@somewhere.ru"));
        assertFalse(accessService.isEmailApproved("guy@somewhere.ch"));
        assertFalse(accessService.isEmailApproved("guy@mail.eamale.com"));
        assertFalse(accessService.isEmailApproved("guy@rerere@qwkcmail.net"));
        assertFalse(accessService.isEmailApproved("guy@dfoofmail.com"));
    }


    @Test
    public void AccessDtoTests() {

        AccessDTO accessDTO;

        // good.com domain is valid and is approved. Passes with multiple subdomains

        accessDTO = accessService.createAccessDTO("johnny@anywhere.good.com");
        assertEquals(accessDTO.getDomain(), "good.com");
        assertTrue(accessDTO.isValid());
        assertTrue(accessDTO.isApproved());

        // printemailtext.com on blacklist. Email valid but domain is not

        accessDTO = accessService.createAccessDTO("putz@ggg.printemailtext.com");
        assertEquals(accessDTO.getDomain(), "printemailtext.com");
        assertTrue(accessDTO.isValid());
        assertFalse(accessDTO.isApproved());

        // .ru TLD is blacklisted. Valid email address but not approved

        accessDTO = accessService.createAccessDTO("putz@anywhere.ru");
        assertTrue(accessDTO.isValid());
        assertFalse(accessDTO.isApproved());

        // Badly formatted email address fails

        accessDTO = accessService.createAccessDTO("printemailtext.com");
        assertFalse(accessDTO.isValid());
        assertFalse(accessDTO.isApproved());

        // Badly formatted email domain fails

        accessDTO = accessService.createAccessDTO("putz@printemailtext");
        assertFalse(accessDTO.isValid());
        assertFalse(accessDTO.isApproved());

        // Other test emails from demo site which fail

        assertFalse(accessService.createAccessDTO("sdsds@mail.eamale.com").isApproved());
        assertFalse(accessService.createAccessDTO("rerere@qwkcmail.net").isApproved());
        assertFalse(accessService.createAccessDTO("abes@dfoofmail.com").isApproved());

        // gmail.com and hotmail.com are blacklisted with endsWith("mail.com") but in Override Property

        assertTrue(accessService.createAccessDTO("somebody@gmail.com").isApproved());
        assertTrue(accessService.createAccessDTO("somebody@hotmail.com").isApproved());

    }

}
