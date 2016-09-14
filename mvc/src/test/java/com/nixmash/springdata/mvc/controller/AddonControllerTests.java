package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.service.AddonService;
import com.nixmash.springdata.mail.service.FmService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static com.nixmash.springdata.mvc.controller.AddonController.FLASHCARDS_VIEW;
import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.csrf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
public class AddonControllerTests  extends AbstractContext {

    @Autowired
    AddonService addonService;

    private AddonController addonController;

    private MockMvc mvc;

    @Autowired
    ApplicationSettings applicationSettings;

    @Autowired
    FmService fmService;

    @Autowired
    WebUI webUI;

    @Before
    public void setUp() {
        addonController = new AddonController(addonService, webUI, applicationSettings, fmService);
        mvc = MockMvcBuilders.standaloneSetup(addonController).build();
    }

    @Test
    public void flashcards_page_loads_with_models() throws Exception {
        RequestBuilder request = get("/posts/flashcards").with(csrf());
        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(FLASHCARDS_VIEW)).andReturn();

        assertThat(result.getModelAndView().getModel().get("flashcards"),
                is(instanceOf(ArrayList.class)));

    }

    @Test
    public void resetPasswordUnmatchedPasswords() throws Exception {
        RequestBuilder request = post("/posts/flashcards")
                .param("categoryId", "2").with(csrf());

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(FLASHCARDS_VIEW)).andReturn();
    }
}
