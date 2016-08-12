package com.nixmash.springdata.mvc.controller;

import com.github.dandelion.core.web.DandelionFilter;
import com.nixmash.springdata.jpa.common.SiteOptions;
import com.nixmash.springdata.jpa.dto.SiteOptionMapDTO;
import com.nixmash.springdata.jpa.service.SiteService;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import com.nixmash.springdata.mvc.security.WithAdminUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import java.util.ArrayList;

import static com.nixmash.springdata.mvc.controller.AdminAddonsController.ADMIN_FLASHCARDS_VIEW;
import static com.nixmash.springdata.mvc.controller.AdminAddonsController.ADMIN_FLASHCARD_CATEGORIES_VIEW;
import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.csrf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
public class AdminAddonsControllerTests extends AbstractContext {

    private AdminController adminController;
    private UserService mockUserService;
    private DandelionFilter dandelionFilter;

    private static final String NEW_SITE_NAME = "New Site Name";
    private static final Integer NEW_INTEGER_PROPERTY = 8;


    @Autowired
    private WebUI webUI;

    @Autowired
    private UserService userService;

    @Autowired
    private SiteOptions siteOptions;

    @Autowired
    private SiteService siteService;

    private MockMvc mvc;
    private SiteOptionMapDTO siteOptionMapDTO;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() throws ServletException {

        this.dandelionFilter = new DandelionFilter();
        this.dandelionFilter.init(new MockFilterConfig());

        mvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .addFilters(dandelionFilter)
                .build();

        mockUserService = mock(UserService.class);
        adminController = new AdminController(userService, webUI, siteOptions, siteService);
    }

    @After
    public void tearDown() {
    }


    @Test
    @WithAdminUser
    public void flashcards_Page_Loads_And_Has_Empty_Flashcard_Model() throws Exception {
        RequestBuilder request = get("/admin/addons/flashcards").with(csrf());
        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_FLASHCARDS_VIEW)).andReturn();

        assertTrue(result.getModelAndView().getModel().containsKey("flashcard"));
    }

    @Test
    @WithAdminUser
    public void flashcard_Categories_Page_Loads_And_Has_FlashCard_Categories() throws Exception {
        RequestBuilder request = get("/admin/addons/flashcards/categories").with(csrf());
        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_FLASHCARD_CATEGORIES_VIEW)).andReturn();

        assertThat(result.getModelAndView().getModel().get("flashcardCategories"),
                is(instanceOf(ArrayList.class)));
    }

}
