package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.mail.service.TemplateService;
import com.nixmash.springdata.mvc.AbstractContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Created by daveburke on 5/27/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PostsRestControllerTests extends AbstractContext {

    private PostsRestController mockPostsRestController;

    @Autowired
    PostService postService;

    @Autowired
    TemplateService templateService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockPostsRestController = new PostsRestController(postService, templateService);
        mockMvc = MockMvcBuilders.standaloneSetup(mockPostsRestController).build();
    }

    @Test
    public void keyValueJson() throws Exception {
        mockMvc.perform(get("/json/posts/map"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void postsInHtmlContentType() throws Exception {
        mockMvc.perform(get("/json/posts/page/1"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}
