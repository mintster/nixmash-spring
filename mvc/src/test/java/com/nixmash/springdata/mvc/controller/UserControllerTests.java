package com.nixmash.springdata.mvc.controller;

import com.github.dandelion.core.web.DandelionFilter;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;

import static com.nixmash.springdata.mvc.controller.UserController.USER_CHANGEPASSWORD_VIEW;
import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.csrf;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTests extends AbstractContext {

    private AdminController adminController;
    private UserService mockUserService;
    private DandelionFilter dandelionFilter;

    @Autowired
    private WebUI webUI;

    @Autowired
    private UserService userService;

    private MockMvc mvc;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() throws ServletException {

        mvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .build();

        mockUserService = mock(UserService.class);
    }

    @Test
    @WithUserDetails(value = "keith", userDetailsServiceBeanName = "currentUserDetailsService")
    public void loggedInUserCanAccessResetPasswordPage() throws Exception {
        RequestBuilder request = get("/users/resetpassword").with(csrf());
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userPasswordDTO"))
                .andExpect(view().name(USER_CHANGEPASSWORD_VIEW));
    }

    @Test
    @WithAnonymousUser
    public void anonymousUserCannotAccessResetPasswordPage() throws Exception {
        RequestBuilder request = get("/users/resetpassword").with(csrf());
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(loginPage());
    }

    @Test
    public void resetPasswordByRegisteredUser() throws Exception {
        RequestBuilder request = post("/users/resetpassword")
                .param("userId", "2").with(csrf());

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.flash().attributeExists("feedbackMessage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("userPasswordDTO"))
                .andDo(print())
                .andExpect(redirectedUrl("users/password"));

    }

    private static ResultMatcher loginPage() {
        return result -> {
            status().isFound().match(result);
            redirectedUrl("http://localhost/signin").match(result);
        };
    }

}
