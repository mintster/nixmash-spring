package com.nixmash.springdata.jsoup;

import com.nixmash.springdata.jsoup.components.JsoupUI;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class JsoupLauncher {

	public static void main(String[] args) {

		ApplicationContext ctx = new
				AnnotationConfigApplicationContext("com.nixmash.springdata.jsoup",
																										"com.nixmash.springdata.jpa");

		JsoupUI ui = ctx.getBean(JsoupUI.class);
		ui.init();
		((ConfigurableApplicationContext) ctx).close();
	}
}
