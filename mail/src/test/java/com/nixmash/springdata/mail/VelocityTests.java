package com.nixmash.springdata.mail;

import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.mail.service.TemplateService;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.velocity.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@RunWith(SpringJUnit4ClassRunner.class)
@SuppressWarnings("deprecation")
public class VelocityTests extends MailContext {

    @Autowired
    Environment environment;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    PostService postService;

    @Autowired
    TemplateService templateService;

    @Test
    public void testContactTemplateContents() throws MessagingException, IOException {
        String siteName = environment.getProperty("mail.contact.site.name");
        Map<String, Object> model = new Hashtable<>();
        model.put("siteName", siteName);
        String result =
                VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "contact.vm", "UTF-8", model);
        assertThat(result, containsString(siteName));
    }

    @Test
    public void postTagWithSpaceIsEncoded() throws PostNotFoundException {

        // H2Post contains the tag "h2 Tag With Spaces"

        Post post = postService.getPostById(5L);
        assertThat(templateService.createPostHtml(post), containsString("h2+tag+with+spaces"));

    }

    @Test
    public void fileUploadScriptTest() {
        String result = templateService.getFileUploadingScript();
        System.out.println(result);
    }
}
