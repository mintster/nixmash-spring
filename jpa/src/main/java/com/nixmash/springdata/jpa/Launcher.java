package com.nixmash.springdata.jpa;

import com.nixmash.springdata.jpa.components.ContactUI;
import com.nixmash.springdata.jpa.config.ApplicationConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.SpringVersion;

import java.util.Date;

@SpringBootApplication
public class Launcher {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ApplicationConfig.class);
		ctx.refresh();
		System.out.println("Spring Framework Version: " + SpringVersion.getVersion());
		System.out.println("Spring Boot Version: " + SpringBootVersion.getVersion());
		for (int i = 0; i <  4; i++) {
			System.out.println("UUID: " + RandomStringUtils.randomAlphanumeric(16));
		}
		System.out.println("new Date().getTime() = " + new Date().getTime());
		ContactUI ui = ctx.getBean(ContactUI.class);
		ui.init();
		ctx.close();
	}

}
