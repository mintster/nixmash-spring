package com.nixmash.springdata.jpa;

import com.nixmash.springdata.jpa.components.JpaUI;
import com.nixmash.springdata.jpa.config.ApplicationConfig;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.SpringVersion;

@SpringBootApplication
public class JpaLauncher {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ApplicationConfig.class);
		ctx.refresh();
		System.out.println("Spring Framework Version: " + SpringVersion.getVersion());
		System.out.println("Spring Boot Version: " + SpringBootVersion.getVersion());
		JpaUI ui = ctx.getBean(JpaUI.class);
		ui.init();
		ctx.close();
	}

}
