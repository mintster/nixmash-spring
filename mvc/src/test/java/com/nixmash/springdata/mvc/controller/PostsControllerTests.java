package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.nixmash.springdata.mvc.controller.PostsController.POSTS_LIST_VIEW;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by daveburke on 5/27/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PostsControllerTests extends AbstractContext {

    private PostsController mockPostsController;

    private MockMvc mockMvc;

    @Autowired
    WebUI webUI;

    @Before
    public void setUp() {
        mockPostsController = new PostsController(webUI);
        mockMvc = MockMvcBuilders.standaloneSetup(mockPostsController).build();
    }

    @Test
    public void homePageTest() throws Exception {
        mockMvc.perform(get("/links"))
                .andExpect(view().name(POSTS_LIST_VIEW));
    }

}
