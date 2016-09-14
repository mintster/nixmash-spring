package com.nixmash.springdata.mail.components;

import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mail.dto.MailDTO;
import com.nixmash.springdata.mail.service.FmService;
import com.nixmash.springdata.mail.service.FmMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class MailUI {

    private static final Logger logger = LoggerFactory.getLogger(MailUI.class);

    private final FmMailService fmMailService;
    private final UserService userService;
    private final FmService fmService;

    @Autowired
    public MailUI(FmMailService fmMailService, UserService userService, FmService fmService) {
        this.fmMailService = fmMailService;
        this.userService = userService;
        this.fmService = fmService;
    }

    public void init() {
        displayUserTemplate();
    }

    private void displayUserTemplate() {
        Optional<User> user = userService.getUserById(8L);
        if (user.isPresent()) {
                System.out.println(fmService.displayTestTemplate(user.get()));
        }
    }

    private void passwordResetDemo() {
        Optional<User> user = userService.getUserById(8L);
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            user.get().setEmail("daveburke@localhost");
            fmMailService.sendResetPasswordMail(user.get(), token);
        }
    }

    private void contactDemo() {
        fmMailService.sendContactMail(createContactMailDTO());
    }


    private MailDTO createContactMailDTO() {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setFrom("contact@aol.com");
        mailDTO.setFromName("Contact Dude");
        mailDTO.setBody("This is a message from a contact");
        return  mailDTO;
    }

}
