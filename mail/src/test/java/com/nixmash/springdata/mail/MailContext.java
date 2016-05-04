package com.nixmash.springdata.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MailLauncher.class)
@ActiveProfiles("h2")
@TestPropertySource("classpath:/test.properties")
public class MailContext {

	@Test
	public void contextLoads() {
	}
}
