package com.nixmash.springdata.mail.components;

import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mail.dto.MailDTO;
import com.nixmash.springdata.mail.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class MailUI {

    private static final Logger logger = LoggerFactory.getLogger(MailUI.class);

    private final MailService mailService;
    private final UserService userService;

    @Autowired
    public MailUI(MailService mailService, UserService userService) {
        this.mailService = mailService;
        this.userService = userService;
    }

    public void init() {
        passwordResetDemo();
    }

    private void passwordResetDemo() {
        Optional<User> user = userService.getUserById(8L);
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            user.get().setEmail("daveburke@localhost");
            mailService.sendResetPasswordMail(user.get(), token);
        }
    }

    private void contactDemo() {
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
