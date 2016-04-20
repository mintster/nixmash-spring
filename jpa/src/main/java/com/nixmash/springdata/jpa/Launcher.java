package com.nixmash.springdata.jpa;

import com.nixmash.springdata.jpa.common.ContactUI;
import com.nixmash.springdata.jpa.config.ApplicationConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.SpringVersion;

@SpringBootApplication
public class Launcher {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ApplicationConfig.class);
		ctx.refresh();
		System.out.println("version: " + SpringVersion.getVersion());
		for (int i = 0; i <  10; i++) {
			System.out.println("UUID: " + RandomStringUtils.randomAlphanumeric(16));
		}
		ContactUI ui = ctx.getBean(ContactUI.class);
		ui.init();
		ctx.close();
	}

}
