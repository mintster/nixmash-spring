package com.nixmash.springdata.mail.components;

import com.nixmash.springdata.mail.dto.MailDTO;
import com.nixmash.springdata.mail.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailUI {

    private static final Logger logger = LoggerFactory.getLogger(MailUI.class);

    private final MailService mailService;

    @Autowired
    public MailUI( MailService mailService) {
        this.mailService = mailService;
    }

    public void init() {
        mailDemo();
    }

    public void mailDemo() {
        System.out.println("\n\n\nCooking with Spring Mail Juice!");
        mailService.sendContactMail(createContactMailDTO());
    }

    private MailDTO createContactMailDTO() {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setFrom("contact@aol.com");
        mailDTO.setFromName("Contact Dude");
        mailDTO.setBody("This is a message from a contact");
        return  mailDTO;
    }

}
