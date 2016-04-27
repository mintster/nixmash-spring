package com.nixmash.springdata.mail;

import com.nixmash.springdata.mail.components.MailUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MailLauncher {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(MailLauncher.class, args);
		MailUI ui = ctx.getBean(MailUI.class);
		ui.init();
		((ConfigurableApplicationContext) ctx).close();
	}

}
