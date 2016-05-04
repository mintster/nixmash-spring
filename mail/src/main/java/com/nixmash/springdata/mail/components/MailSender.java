package com.nixmash.springdata.mail.components;

import com.nixmash.springdata.mail.common.MailSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class MailSender extends JavaMailSenderImpl {

    @Autowired
    MailSettings mailSettings;

    @Override
    public String getHost() {
        return mailSettings.getServerHost();
    }

    @Override
    public int getPort() {
        return mailSettings.getServerPort();
    }

    @Override
    public String getUsername() { return  mailSettings.getServerUsername(); }

    @Override
    public String getPassword() { return mailSettings.getServerPassword(); }

    @Override
    public Properties getJavaMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", mailSettings.getSmtpAuth().toString());
        properties.setProperty("mail.smtp.starttls.enable", mailSettings.getSmtpStartTlsEnable().toString());
        return properties;
        }

}
