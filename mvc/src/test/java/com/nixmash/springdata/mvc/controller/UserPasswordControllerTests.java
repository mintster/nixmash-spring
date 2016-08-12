package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.model.UserToken;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import java.util.Optional;
import java.util.UUID;

import static com.nixmash.springdata.mvc.controller.UserPasswordController.USER_CHANGEPASSWORD_VIEW;
import static com.nixmash.springdata.mvc.controller.UserPasswordController.USER_FORGOTPASSWORD_VIEW;
import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.csrf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
public class UserPasswordControllerTests extends AbstractContext {

    private UserService mockUserService;

    @Autowired
    private WebUI webUI;

    @Autowired
    private UserService userService;

    private MockMvc mvc;
    private User erwin;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() throws ServletException {
        erwin = userService.getUserByUsername("erwin");
        mvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .build();

        mockUserService = mock(UserService.class);
    }

    @Test
    public void getForgotEmailView() throws Exception {
        RequestBuilder request = get("/users/forgotpassword").with(csrf());
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("forgotEmailDTO"))
                .andExpect(view().name(USER_FORGOTPASSWORD_VIEW));
    }

    @Test
    public void emptyForgotPasswordPost() throws Exception {

        RequestBuilder request = post("/users/forgotpassword")
                .param("email", "").with(csrf());

        mvc.perform(request)
                .andExpect(model()
                        .attributeHasFieldErrorCode("forgotEmailDTO", "email", containsString("NotEmpty")));
    }

    @Test
    public void badEmailForgotPasswordPost() throws Exception {

        RequestBuilder request = post("/users/forgotpassword")
                .param("email", "bad@email.com").with(csrf());

        mvc.perform(request)
                .andExpect(model().attributeHasErrors("forgotEmailDTO"));
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
    @WithAnonymousUser
    public void resetPasswordPageWithTokenReturnsChangePasswordView() throws Exception {
        RequestBuilder request = get("/users/resetpassword/" + UUID.randomUUID().toString()).with(csrf());
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userPasswordDTO"))
                .andExpect(view().name(USER_CHANGEPASSWORD_VIEW));
    }

    @Test
    @WithAnonymousUser
    public void resetPasswordFromEmail() throws Exception {

        UserToken userToken = userService.createUserToken(erwin);
        RequestBuilder request = post("/users/resetpassword")
                .param("userId", "-400")
                .param("verificationToken", userToken.getToken())
                .param("password", "password")
                .param("repeatedPassword", "password").with(csrf());
        mvc.perform(request)
                .andExpect(model().attribute("feedbackMessage", containsString("login")));
    }


    @Test
    public void resetPasswordMatchingPasswords() throws Exception {
        RequestBuilder request = post("/users/resetpassword")
                .param("userId", "4").param("password", "password").param("repeatedPassword", "password").with(csrf());

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("feedbackMessage"))
                .andExpect(model().attributeExists("userPasswordDTO"))
                .andExpect(view().name(USER_CHANGEPASSWORD_VIEW));

    }

    @Test
    public void resetPasswordUnmatchedPasswords() throws Exception {
        RequestBuilder request = post("/users/resetpassword")
                .param("userId", "4")
                .param("password", "password1")
                .param("repeatedPassword", "password2").with(csrf());

        mvc.perform(request)
                .andExpect(model().attributeHasErrors("userPasswordDTO"));
    }

    @Test
    public void resetPasswordWithBlankPasswords() throws Exception {
        RequestBuilder request = post("/users/resetpassword")
                .param("userId", "4")
                .param("password", "")
                .param("repeatedPassword", "").with(csrf());

        mvc.perform(request)
                .andExpect(model().attributeHasErrors("userPasswordDTO"));

    }

    @Test
    public void loggedInUserPasswordIsUpdated() throws Exception {
        RequestBuilder request = post("/users/resetpassword")
                .param("userId", "4")
                .param("password", "password")
                .param("repeatedPassword", "password").with(csrf());

        mvc.perform(request);

        Optional<User> user = userService.getUserById(4L);
        if (user.isPresent()) {
            String encodedPassword = user.get().getPassword();
            assertTrue(new BCryptPasswordEncoder().matches("password", encodedPassword));
        }
    }

    private static ResultMatcher loginPage() {
        return result -> {
            status().isFound().match(result);
            redirectedUrl("http://localhost/signin").match(result);
        };
    }


}
