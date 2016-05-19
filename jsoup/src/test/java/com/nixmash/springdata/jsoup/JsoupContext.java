package com.nixmash.springdata.jsoup;

import com.nixmash.springdata.mail.MailLauncher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MailLauncher.class)
@ActiveProfiles("h2")
@TestPropertySource("classpath:/jsoup.properties")
public class JsoupContext {

    @Test
    public void contextLoads() {
    }
}
