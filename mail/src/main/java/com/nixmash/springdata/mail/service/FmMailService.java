package com.nixmash.springdata.mail.service;

import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.mail.dto.MailDTO;

/**
 * Created by daveburke on 4/28/16.
 */
public interface FmMailService {

    void sendResetPasswordMail(User user, String token);

    void sendContactMail(MailDTO mailDTO);

}
