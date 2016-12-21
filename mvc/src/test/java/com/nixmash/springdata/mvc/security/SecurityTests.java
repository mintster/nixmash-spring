package com.nixmash.springdata.mvc.security;

import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.controller.UserController;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.Filter;

import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.csrf;
import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.user;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Rob Winch
 */
@RunWith(SpringRunner.class)
public class SecurityTests extends AbstractContext {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Autowired
	private Filter springSecurityFilterChain;

	@Autowired
	private CurrentUserDetailsService currentUserDetailsService;

	@Autowired
	private UserService userService;

	private CurrentUser keith;
	private CurrentUser user;
	private CurrentUser admin;

	private MockMvc mvc;

	@Before
	public void setup() {

		keith = currentUserDetailsService.loadUserByUsername("keith");
		user = currentUserDetailsService.loadUserByUsername("user");
		admin = currentUserDetailsService.loadUserByUsername("admin");

		mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build();

	}

	// region Login

	@Test
	public void invalidUsernamePassword() throws Exception {
		mvc.perform(formLogin("/signin/authenticate")
				.user("user").password("nope"))
				.andExpect(unauthenticated());
	}

	@Test
	public void validUsernamePassword() throws Exception {
		mvc.perform(formLogin("/signin/authenticate")
				.user("user").password("password"))
				.andExpect(authenticated());
	}

	@Test
	public void unEnabledUserIsNotAuthenticatedOnLogin() throws Exception {
		// tommy.isEnabled(0) | approvedDateTime(null)

		mvc.perform(formLogin("/signin/authenticate")
				.user("tommy").password("password"))
				.andExpect(unauthenticated());
	}

	// endregion

	// region Contact Details

	@Test
	public void redirectOnContactDetails() throws Exception {
		RequestBuilder request = post("/contact/1").with(csrf());
		mvc.perform(request).andExpect(loginPage());
	}

	// endregion

	// region User Profiles

	@Test
	public void userCannotAccessAnotherProfile() throws Exception {
		RequestBuilder request = get("/{username}", "user").with(user(keith)).with(csrf());
		mvc.perform(request).andExpect(status().isForbidden());
	}

	@Test
	public void userCanAccessOwnProfile() throws Exception {
		RequestBuilder request = get("/{username}", "user").with(user(user)).with(csrf());
		mvc.perform(request).andExpect(status().isOk()).andExpect(view().name(UserController.USER_PROFILE_VIEW));
	}

	@Test
	public void profileRequiresCsrf() throws Exception {
		RequestBuilder request = post("/").with(user(keith));
		mvc.perform(request).andExpect(invalidCsrf());
	}

	// endregion

	// region Registration Form

	@Test
	public void validRegistration_UserCreatedButNotApproved() throws Exception {
		mvc.perform(post("/register")
				.param("username", "bobby").param("firstName", "Bob")
				.param("lastName", "Crachet").param("email", "bob@aol.com")
				.param("password", "password")
				.param("repeatedPassword", "password").with(csrf()));

		User bobby = userService.getUserByUsername("bobby");
		assertNotNull(bobby.getUserData().getCreatedDatetime());
		assertNull(bobby.getUserData().getApprovedDatetime());
		assertFalse(bobby.isEnabled());
	}

	@Test
	public void invalidRegistrationEmail() throws Exception {
		RequestBuilder request = post("/register").param("username", "bobby").param("firstName", "Bob")
				.param("lastName", "Crachet").param("email", "bad@email").param("password", "password")
				.param("repeatedPassword", "password").with(csrf());
		mvc.perform(request).andExpect(invalidRegistration());
	}

	@Test
	public void preExistingUsernameRegistration() throws Exception {
		RequestBuilder request = post("/register").param("username", "user").param("firstName", "Bob")
				.param("lastName", "Crachet").param("email", "bob@email.com").param("password", "password")
				.param("repeatedPassword", "password").with(csrf());
		mvc.perform(request).andExpect(model().attributeHasErrors("userDTO")).andExpect(invalidRegistration());
	}

	@Test
	public void unapprovedEmailDomainFails() throws Exception {
		RequestBuilder request = post("/register").param("username", "user143").param("firstName", "Bob")
				.param("lastName", "Crachet").param("email", "bob@somewhere.ru").param("password", "password")
				.param("repeatedPassword", "password").with(csrf());
		 mvc.perform(request).andExpect(model().attributeHasErrors("userDTO")).andExpect(invalidRegistration());
	}

	@Test(expected = DisabledException.class)
	public void gmailDomainPassesFilter_IsNotYetApproved() throws Exception {
		RequestBuilder request = post("/register").param("username", "user1215155").param("firstName", "Bob")
				.param("lastName", "Crachet").param("email", "bob@gmail.com").param("password", "password")
				.param("repeatedPassword", "password").with(csrf());

		mvc.perform(request).andExpect(status().is3xxRedirection());

		// user is disabled. Where it throws DisabledException
		CurrentUser currentUser1215155 = currentUserDetailsService.loadUserByUsername("user1215155");


	}


	// endregion

	// region Contact Form

	@Test
	public void getContactForm() throws Exception {

		RequestBuilder request = get("/contact/update/1").with(user(admin)).with(csrf());

		mvc.perform(request).andExpect(status().isOk()).andExpect(view().name("contacts/contactform"));

	}

	// endregion

	// region ResultMatchers

	private static ResultMatcher loginPage() {
		return result -> {
			status().isFound().match(result);
			redirectedUrl("http://localhost/signin").match(result);
		};
	}

	private static ResultMatcher invalidLogin() {
		return result -> {
			status().isFound().match(result);
			redirectedUrl("/signin?error").match(result);
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

	// endregion
}