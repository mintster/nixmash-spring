package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.dto.GitHubDTO;
import com.nixmash.springdata.mail.service.TemplateService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.nixmash.springdata.mvc.controller.GeneralController.HOME_VIEW;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class GeneralControllerTests extends AbstractContext {

    GeneralController mockController;

    private MockMvc mockMvc;

    @Autowired
    TemplateService templateService;

    @Autowired
    WebUI webUI;

    @Before
    public void setUp() {
        mockController = new GeneralController(templateService, webUI);
        mockMvc = MockMvcBuilders.standaloneSetup(mockController).build();
    }

    @Test
    public void homePageTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("gitHubStats", isA(GitHubDTO.class)))
                .andExpect(view().name(HOME_VIEW));
    }

    @Test
    public void resourceNotFoundExceptionTest() throws Exception {
        mockMvc.perform(get("/badurl"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void retrieveRobotsTxtFile() throws Exception {
        mockMvc.perform(get("/robots.txt"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string(containsString("Disallow")));

    }
}
