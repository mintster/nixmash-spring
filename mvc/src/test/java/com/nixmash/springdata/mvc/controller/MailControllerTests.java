package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.mail.service.MailService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Map;

import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.csrf;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class MailControllerTests extends AbstractContext {

    private static final String FEEDBACK_MESSAGE = "feedbackMessage";

    private MockMvc mockMvc;
    private MailService mockMailService;
    private MessageSource mockMessageSource;

    MailController mockMailController;

    @Autowired
    WebUI webUI;

    @Before
    public void setUp() {

        final ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver();
        final StaticApplicationContext applicationContext = new StaticApplicationContext();
        applicationContext.registerBeanDefinition("mailController",
                new RootBeanDefinition(MailController.class, null, null));
        exceptionResolver.setApplicationContext(applicationContext);
        exceptionResolver.afterPropertiesSet();

        mockMailService = mock(MailService.class);
        mockMessageSource = mock(MessageSource.class);

        mockMailController = new MailController(webUI, mockMailService);
        mockMvc = MockMvcBuilders.standaloneSetup(mockMailController)
                .setHandlerExceptionResolvers(exceptionResolver).build();


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
        mockMvc.perform(request).andDo(print());

//        RedirectAttributes attributes = new RedirectAttributesModelMap();
//        initMessageSourceForFeedbackMessage(MailController.EMAIL_SENT_MESSAGE_KEY);
//        assertFeedbackMessage(attributes, MailController.EMAIL_SENT_MESSAGE_KEY);
//
        mockMvc.perform(request)
                .andExpect(redirectedUrl("/users/contact"));

    }


    private void initMessageSourceForFeedbackMessage(String feedbackMessageCode) {
        when(mockMessageSource.getMessage(eq(feedbackMessageCode), any(Object[].class), any(Locale.class))).thenReturn(FEEDBACK_MESSAGE);
    }


    private void assertFeedbackMessage(RedirectAttributes model, String messageCode) {
        assertFlashMessages(model, messageCode, WebUI.FLASH_MESSAGE_KEY_FEEDBACK);
    }

    private void assertFlashMessages(RedirectAttributes model, String messageCode, String flashMessageParameterName) {
        Map<String, ?> flashMessages = model.getFlashAttributes();
        Object message = flashMessages.get(flashMessageParameterName);
        assertNotNull(message);
    }
}
