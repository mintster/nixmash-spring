package com.nixmash.springdata.mail;

import com.nixmash.springdata.jpa.JpaLauncher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MailLauncher.class, JpaLauncher.class},  loader=AnnotationConfigContextLoader.class)
@ActiveProfiles("h2")
@TestPropertySource("classpath:/test.properties")
public class MailContext {

	@Test
	public void contextLoads() {
	}
}