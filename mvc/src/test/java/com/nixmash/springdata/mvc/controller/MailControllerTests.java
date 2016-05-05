package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.mail.service.MailService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.csrf;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class MailControllerTests extends AbstractContext {

    MockMvc mockMvc;
    MailService mockMailService;
    MailController mockMailController;

    @Autowired
    WebUI webUI;

    @Before
    public void setUp() {

        mockMailService = mock(MailService.class);
        mockMailController = new MailController(webUI, mockMailService);
        mockMvc = MockMvcBuilders.standaloneSetup(mockMailController).build();

    }

    @Test
    public void loadContactFormPage() throws Exception {
        mockMvc.perform(get("/users/contact"))
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(MailController.MAIL_CONTACT_VIEW));
    }

    @Test
    public void postContactFormWithBadEmail() throws Exception {

        RequestBuilder request = post("/users/contact")
                .param("from", "bad@emailaddress").param("fromName", "Bob")
                .param("body", "This is my message").with(csrf());

        mockMvc.perform(request)
                .andExpect(model().attributeHasFieldErrors("mailDTO", "from"))
                .andExpect(view().name(MailController.MAIL_CONTACT_VIEW));
    }

    @Test
    public void postSuccessfulContactEmail() throws Exception {
        RequestBuilder request = post("/users/contact")
                .param("from", "good@emailaddress.com").param("fromName", "Bob")
                .param("body", "This is my message").with(csrf());

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.flash().attributeExists("feedbackMessage"))
                .andExpect(redirectedUrl("/users/contact"));

    }

}
