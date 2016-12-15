package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.model.validators.SocialUserFormValidator;
import com.nixmash.springdata.jpa.model.validators.UserCreateFormValidator;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.nixmash.springdata.mvc.controller.GeneralController.HOME_VIEW;
import static com.nixmash.springdata.mvc.controller.GlobalController.ERROR_CUSTOM_VIEW;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by daveburke on 12/15/16.
 */
@RunWith(SpringRunner.class)
public class UserControllerTests extends AbstractContext {

    MockMvc mvc;
    private UserService mockUserService;
    private UserController mockUserController;
    private Optional<User> user;
    private final String GOOD_USERKEY = "GOOD";
    private final String BAD_USERKEY = "BAD";

    @Autowired
    private WebUI webUI;

    @Autowired
    private UserCreateFormValidator  userCreateFormValidator;

    @Autowired
    private SocialUserFormValidator socialUserFormValidator;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {

        mockUserService= mock(UserService.class);
        mockUserController = new UserController(mockUserService,
                userCreateFormValidator,
                socialUserFormValidator,
                providerSignInUtils,
                webUI);
        mvc = MockMvcBuilders.standaloneSetup(mockUserController).build();

        // Scott Shoenberger
        user = userService.getUserById(6);

        when(mockUserService.getByUserKey(GOOD_USERKEY)).thenReturn(user);
        when(mockUserService.getByUserKey(BAD_USERKEY)).thenReturn(Optional.empty());
    }

    @Test
    public void missingUserKeyDisplaysErrorPage() throws Exception {
        MvcResult result = mvc.perform(get("/users/verify"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_CUSTOM_VIEW)).andReturn();

        assertTrue(result.getModelAndView().getModel().containsValue("Missing Authentication Key"));
    }

    @Test
    public void goodUserKeyDisplaysHomePage() throws Exception {
        mvc.perform(get("/users/verify/" + GOOD_USERKEY))
                .andExpect(status().isOk())
                .andExpect(view().name(HOME_VIEW));
    }

    @Test
    public void badUserKeyDisplaysErrorPage() throws Exception {
        mvc.perform(get("/users/verify/" + BAD_USERKEY))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_CUSTOM_VIEW));
    }

    @Test
    public void unEnabledUserIsNotAuthenticatedOnLogin() throws Exception {

    }

}
