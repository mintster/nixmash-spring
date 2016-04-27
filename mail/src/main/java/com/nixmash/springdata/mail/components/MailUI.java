package com.nixmash.springdata.mail.components;

import com.nixmash.springdata.mail.common.MailSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailUI {

	MailSender mailSender;

	@Autowired
	public MailUI(MailSettings mailSettings, MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void init() {
		mailDemo();
	}

	public void mailDemo() {
		System.out.println("\n\n\nCooking with Spring Mail Juice!");
		sendMail();
	}

	public void sendMail() {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setText("yousa!");
		message.setSubject("Yousa subject III!");
		message.setTo("daveburke@johnwayne");
		message.setFrom("root@localhost");

		mailSender.send(message);

	}
}
