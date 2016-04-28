package com.nixmash.springdata.mail.service;

import com.nixmash.springdata.mail.dto.MailDTO;

/**
 * Created by daveburke on 4/28/16.
 */
public interface MailService {

    void sendContactMail(MailDTO mailDTO);

}
