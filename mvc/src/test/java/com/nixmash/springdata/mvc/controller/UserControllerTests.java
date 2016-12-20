package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.model.validators.SocialUserFormValidator;
import com.nixmash.springdata.jpa.model.validators.UserCreateFormValidator;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mail.service.FmMailService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.nixmash.springdata.mvc.controller.GeneralController.REDIRECT_HOME_VIEW;
import static com.nixmash.springdata.mvc.controller.GlobalController.ERROR_CUSTOM_VIEW;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by daveburke on 12/15/16.
 */
@RunWith(SpringRunner.class)
public class UserControllerTests extends AbstractContext {

    MockMvc mvc;
    private UserService mockUserService;
    private UserController mockUserController;
    private Optional<User> toBeApprovedUser;
    private final String BAD_USERKEY = "BAD";
    private final String TO_BE_APPROVED_USERKEY = "TOBEAPPROVED";

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

    @Autowired
    private FmMailService fmMailService;

    @Before
    public void setUp() {

        mockUserService= mock(UserService.class);
        mockUserController = new UserController(mockUserService,
                userCreateFormValidator,
                socialUserFormValidator,
                providerSignInUtils,
                webUI, fmMailService);
        mvc = MockMvcBuilders.standaloneSetup(mockUserController).build();

        // Tommy is not yet approved
        toBeApprovedUser = userService.getUserById(7);

        when(mockUserService.getByUserKey(TO_BE_APPROVED_USERKEY)).thenReturn(toBeApprovedUser);
        when(mockUserService.getByUserKey(BAD_USERKEY)).thenReturn(Optional.empty());

    }

    @Test
    public void badUserKeyDisplaysErrorPage() throws Exception {
        mvc.perform(get("/users/verify/" + BAD_USERKEY))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_CUSTOM_VIEW));
    }

    @Test
    public void toBeApprovedUserKeyDisplaysHomePageWithMessage() throws Exception {
        // Tommy is not yet approved

        mvc.perform(get("/users/verify/" + TO_BE_APPROVED_USERKEY))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("emailVerifiedWelcomeMessage"))
                .andExpect(view().name(REDIRECT_HOME_VIEW));

    }

}
