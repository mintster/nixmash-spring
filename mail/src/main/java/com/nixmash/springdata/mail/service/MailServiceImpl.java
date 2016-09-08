package com.nixmash.springdata.mail.service;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.mail.common.MailSettings;
import com.nixmash.springdata.mail.components.MailSender;
import com.nixmash.springdata.mail.dto.MailDTO;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Map;

@SuppressWarnings({"Convert2Lambda", "deprecation"})
@Service("mailService")
public class MailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
    private static final String CONTACT_EMAIL_SUBJECT = "mail.contact.subject";
    private static final String CONTACT_EMAIL_GREETING = "mail.contact.greeting";
    public static final String EMAIL_SITE_USER_SERVICES = "mail.site.user.services";

    final private MailSender mailSender;
    final private MailSettings mailSettings;
    final private VelocityEngine velocityEngine;
    final private ApplicationSettings applicationSettings;

    @Autowired
    Environment environment;

    @Value("${mail.contact.body.type}")
    private MailDTO.Type mailType;


    @Autowired
    public MailServiceImpl(MailSender mailSender,
                           MailSettings mailSettings, VelocityEngine velocityEngine, ApplicationSettings applicationSettings) {
        this.mailSender = mailSender;
        this.mailSettings = mailSettings;
        this.velocityEngine = velocityEngine;
        this.applicationSettings = applicationSettings;
    }


    @Override
    public void sendResetPasswordMail(User user, String token) {
        try {
            mailSender.send(new MimeMessagePreparator() {
                static final String PASSWORD_RESET_EMAIL_SUBJECT = "mail.password.reset.subject";
                static final String PASSWORD_RESET_EMAIL_FROM = "mail.password.reset.from";
                static final String PASSWORD_RESET_EMAIL_GREETING = "mail.password.reset.greeting";

                public void prepare(MimeMessage mimeMessage)
                        throws MessagingException {

                    // region build mimeMessage

                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                    message.setFrom(environment.getProperty(PASSWORD_RESET_EMAIL_FROM));
                    String sendTo = user.getEmail();
                    if (applicationSettings.getBaseUrl().indexOf("localhost") > 0) {
                        sendTo = mailSettings.getDeveloperTo();
                    }
                    message.addTo(sendTo);

                    message.setSubject(environment.getProperty(PASSWORD_RESET_EMAIL_SUBJECT));

                    String greeting = environment.getProperty(PASSWORD_RESET_EMAIL_GREETING);
                    greeting = MessageFormat.format(greeting, String.format("%s %s", user.getFirstName(), user.getLastName()));

                    String applicationPropertyUrl = environment.getProperty("spring.social.application.url");
                    String siteName = environment.getProperty("mail.site.name");

                    String resetLink = applicationSettings.getBaseUrl() + "/users/resetpassword/" + token;

                    // endregion

                    StringWriter w = new StringWriter();
                    Map<String, Object> model = new Hashtable<String, Object>();
                    model.put("greeting", greeting);
                    model.put("memberServices",  environment.getProperty(EMAIL_SITE_USER_SERVICES));
                    model.put("siteName", siteName);
                    model.put("applicationPropertyUrl", applicationPropertyUrl);
                    model.put("resetLink", resetLink);
                    try {
                        VelocityEngineUtils.mergeTemplate(velocityEngine, "resetpassword.vm", "UTF-8", model, w);
                        message.setText(w.toString(), true);
                    } catch (Exception e) {
                        logger.error("Problem merging template : " + e);
                    }

                    logger.info(String.format("Email sent successfully to: %s", String.format("%s %s", user.getFirstName(), user.getLastName())));
                }

            });
        } catch (MailSendException e) {
            logger.error("Reset Password Email Exception: " + e.getMessage());
        }

    }

    @Override
    public void sendContactMail(MailDTO mailDTO) {
        try {
            mailSender.send(new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage)
                        throws MessagingException {

                    // region build mimeMessage

                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                    message.setFrom(mailDTO.getFrom());
                    message.addTo(mailSettings.getContactTo());

                    if (mailSettings.getSendContactCC()) {
                        message.addCc(mailSettings.getContactCC());
                    }

                    String subject = environment.getProperty(CONTACT_EMAIL_SUBJECT);
                    message.setSubject(MessageFormat.format(subject, mailDTO.getFromName()));

                    String body = mailDTO.getBody();
                    String greeting = environment.getProperty(CONTACT_EMAIL_GREETING);
                    String applicationPropertyUrl = environment.getProperty("spring.social.application.url");
                    String siteName = environment.getProperty("mail.site.name");

                    // endregion

                    switch (mailType) {
                        case HTML:
                            StringWriter w = new StringWriter();
                            Map<String, Object> model = new Hashtable<String, Object>();
                            model.put("message", mailDTO);
                            model.put("greeting", greeting);
                            model.put("siteName", siteName);
                            model.put("applicationPropertyUrl", applicationPropertyUrl);
                            try {
                                VelocityEngineUtils.mergeTemplate(velocityEngine, "contact.vm", "UTF-8", model, w);
                                message.setText(w.toString(), true);
                            } catch (Exception e) {
                                logger.error("Problem merging template : " + e);
                            }

                            break;
                        default:
                            message.setText(body);
                            break;
                    }
                    logger.info(String.format("Email sent successfully from: %s", mailDTO.getFrom()));
                }

            });
        } catch (MailSendException e) {
            logger.error("Contact Email Exception: " + e.getMessage());
        }

    }

}
