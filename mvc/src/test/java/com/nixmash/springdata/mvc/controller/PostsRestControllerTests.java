package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.mvc.AbstractContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
public class PostsRestControllerTests extends AbstractContext {

    @Autowired
    protected WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc =  webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void keyValueJson() throws Exception {
        mockMvc.perform(get("/json/posts/map"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithAnonymousUser
    public void postsInHtmlContentType() throws Exception {
        mockMvc.perform(get("/json/posts/page/1"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}
