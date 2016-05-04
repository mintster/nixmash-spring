package com.nixmash.springdata.mail;

import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@RunWith(SpringJUnit4ClassRunner.class)
public class VelocityTests extends MailContext {

    @Autowired
    Environment environment;

    @Autowired
    private VelocityEngine velocityEngine;

    @Test
    public void testContactTemplateContents() throws MessagingException, IOException {
        String siteName = environment.getProperty("mail.contact.site.name");
        Map<String, Object> model = new Hashtable<>();
        model.put("siteName", siteName);
        String result =
                VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "contact.vm", "UTF-8", model);
        assertThat(result, containsString(siteName));
    }

}
