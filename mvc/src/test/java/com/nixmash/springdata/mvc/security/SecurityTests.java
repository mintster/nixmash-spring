package com.nixmash.springdata.mvc.security;


import com.nixmash.springdata.jpa.model.Authority;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.mvc.AbstractContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.Filter;

import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.csrf;
import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Rob Winch
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SecurityTests extends AbstractContext {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    private Filter springSecurityFilterChain;

    private User rob;
    private User admin;

    private MockMvc mvc;

    @Before
    public void setup() {
        // NOTE: Could also load rob from UserRepository if we wanted
        rob = new User();
        rob.setId(0L);
        rob.setEmail("user@aol.com");
        rob.setUsername("rob");

        admin = new User();
        admin.setId(1L);
        admin.setEmail("admin@example.com");
        admin.setFirstName("Admin");
        admin.setLastName("Dude");
        admin.getAuthorities().add(new Authority("ROLE_ADMIN"));

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }


    @Test
    public void invalidUsernamePassword() throws Exception {
        RequestBuilder request = post("/login")
                .param("username", "user")
                .param("password", "invalid")
                .with(csrf());

        mvc
                .perform(request)
                .andExpect(invalidLogin());
    }

    @Test
    public void validUsernamePassword() throws Exception {
        RequestBuilder request = post("/login")
                .param("username", "user")
                .param("password", "password")
                .with(csrf());

        mvc
                .perform(request)
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void redirectOnContactDetails() throws Exception {
        RequestBuilder request = post("/contact/1")
                .with(csrf());

        mvc
                .perform(request)
                .andExpect(loginPage());
    }

    @Test
    public void composeRequiresCsrf() throws Exception {
        RequestBuilder request = post("/")
                .with(user(rob).roles("USER"));

        mvc
                .perform(request)
                .andExpect(invalidCsrf());
    }

    @Test
    public void userCannotAccessConsole() throws Exception {
        RequestBuilder request = get("/console")
                .with(user(rob).roles("USER"));

        mvc
                .perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    public void validRegistration() throws Exception {
        RequestBuilder request = post("/register")
                .param("username", "bobby")
                .param("firstName", "Bob")
                .param("lastName", "Crachet")
                .param("email", "bob@aol.com")
                .param("password", "password")
                .param("repeatedPassword", "password")
                .with(csrf());

        mvc
                .perform(request)
                .andExpect(redirectedUrl("/contacts"));
    }

    @Test
    public void invalidRegistrationEmail() throws Exception {
        RequestBuilder request = post("/register")
                .param("username", "bobby")
                .param("firstName", "Bob")
                .param("lastName", "Crachet")
                .param("email", "user")
                .param("password", "password")
                .param("repeatedPassword", "password")
                .with(csrf());
        mvc
                .perform(request)
                .andExpect(invalidRegistration());
    }

    @Test
    public void preExistingUsernameRegistration() throws Exception {
        RequestBuilder request = post("/register")
                .param("username", "user")
                .param("firstName", "Bob")
                .param("lastName", "Crachet")
                .param("email", "bob@email.com")
                .param("password", "password")
                .param("repeatedPassword", "password")
                .with(csrf());
        mvc
                .perform(request)
                .andExpect(model().attributeHasErrors("userDTO"))
                .andExpect(invalidRegistration())
                .andDo(print());
    }

    private static ResultMatcher loginPage() {
        return result -> {
            status().isFound().match(result);
            redirectedUrl("http://localhost/login").match(result);
        };
    }

    private static ResultMatcher invalidLogin() {
        return result -> {
            status().isFound().match(result);
            redirectedUrl("/login?error").match(result);
        };
    }

    private static ResultMatcher invalidRegistration() {
        return result -> {

            status().isOk().match(result);
            view().name("register").match(result);
        };
    }

    private static ResultMatcher invalidCsrf() {
        return result -> status().isForbidden().match(result);
    }


}