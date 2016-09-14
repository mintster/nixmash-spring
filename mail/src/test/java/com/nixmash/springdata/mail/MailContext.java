package com.nixmash.springdata.mail;

import com.nixmash.springdata.jpa.JpaLauncher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={MailLauncher.class, JpaLauncher.class})
@ActiveProfiles("h2")
@TestPropertySource("classpath:/test.properties")
public class MailContext {

	@Test
	public void contextLoads() {
	}
}