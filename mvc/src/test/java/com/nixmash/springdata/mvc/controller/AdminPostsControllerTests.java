package com.nixmash.springdata.mvc.controller;

import com.github.dandelion.core.web.DandelionFilter;
import com.nixmash.springdata.jpa.service.PostService;
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

import static com.nixmash.springdata.mvc.controller.AdminPostsController.*;
import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.csrf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
public class AdminPostsControllerTests extends AbstractContext {

    private DandelionFilter dandelionFilter;

    private static final String GOOD_URL = "http://nixmash.com/java/dysfunctional-enumerated-annotations-in-hibernate/";

    @Autowired
    private WebUI webUI;

    @Autowired
    private PostService postService;

    private MockMvc mvc;

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

        postService = mock(PostService.class);
    }

    @After
    public void tearDown() {
    }


    @Test
    @WithAdminUser
    public void postsList_page_loads() throws Exception {
        RequestBuilder request = get("/admin/posts").with(csrf());
        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_POSTS_LIST_VIEW)).andReturn();

        assertThat(result.getModelAndView().getModel().get("posts"),
                is(instanceOf(ArrayList.class)));
    }

    @Test
    @WithAdminUser
    public void addPost_page_loads() throws Exception {
        RequestBuilder request = get("/admin/posts/add/post").with(csrf());
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postDTO"))
                .andExpect(view().name(ADMIN_POST_ADD_VIEW));

    }

    @Test
    @WithAdminUser
    public void addLink_page_loads() throws Exception {
        RequestBuilder request = get("/admin/posts/add/link").with(csrf());
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postDTO"))
                .andExpect(view().name(ADMIN_LINK_ADD_VIEW));
    }

    @Test
    @WithAdminUser
    public void addLink_page_loads_with_Url() throws Exception {
        mvc.perform(get("/admin/posts/add/link")
                .param("isUrl", "true")
                .param("link", GOOD_URL).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("hasLink"))
                .andExpect(view().name(ADMIN_LINK_ADD_VIEW));
    }

    @Test
    @WithAdminUser
    public void addLink_page_returns_error_on_missing_Url() throws Exception {
        mvc.perform(get("/admin/posts/add/link")
                .param("isUrl", "true")
                .param("link", "").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model()
                        .attributeHasFieldErrorCode("postLink", "link", "post.link.is.empty"))
                .andExpect(view().name(ADMIN_LINK_ADD_VIEW));
    }

    @Test
    @WithAdminUser
    public void updatePostLink_page_loads() throws Exception {
        RequestBuilder request = get("/admin/posts/update/1").with(csrf());
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postDTO"))
                .andExpect(view().name(ADMIN_POSTLINK_UPDATE_VIEW));
    }


}
