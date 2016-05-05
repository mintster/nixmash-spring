package com.nixmash.springdata.mail;

import com.nixmash.springdata.mail.common.MailSettings;
import com.nixmash.springdata.mail.components.MailSender;
import com.nixmash.springdata.mail.dto.MailDTO;
import com.nixmash.springdata.mail.service.MailService;
import com.nixmash.springdata.mail.service.MailServiceImpl;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.MessagingException;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class MailTests extends MailContext {

    private MailSender mockMailSender;
    private MailDTO mailDTO;

    private MailService mockMailService;
    private MailSettings mailSettings;
    private VelocityEngine velocityEngine;

    @Before
    public void setUp() {
        mockMailSender = mock(MailSender.class);
        mockMailService =
                new MailServiceImpl(mockMailSender, mailSettings, velocityEngine);
        mailDTO = MailTestUtils.testMailDTO();
    }

    @Test
    public void mailSenderSendsMimeMessage() throws MessagingException {
        mockMailService.sendContactMail(mailDTO);
        verify(mockMailSender, Mockito.times(1)).send(any(MimeMessagePreparator.class));
    }


}
