/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nixmash.springdata.mvc.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.nixmash.springdata.jpa.dto.SocialUserDTO;
import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.enums.SocialMediaService;
import com.nixmash.springdata.jpa.model.Authority;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.validators.SocialUserFormValidator;
import com.nixmash.springdata.jpa.model.validators.UserCreateFormValidator;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.security.CurrentUserDetailsService;

/**
 * Allows users to sign up.
 *
 * @author Rob Winch
 */
@Controller
public class UserController {

	public static final String MODEL_ATTRIBUTE_CURRENTUSER = "currentUser";
	private static final String MODEL_ATTRIBUTE_SOCIALUSER = "socialUserDTO";
	public static final String USER_PROFILE_VIEW = "users/profile";
	public static final String SIGNUP_VIEW = "signup";
	public static final String SIGNIN_VIEW = "signin";
	public static final String REGISTER_VIEW = "register";

	private final UserService userService;
	private final CurrentUserDetailsService currentUserDetailsService;
	private final UserCreateFormValidator userCreateFormValidator;
	private final SocialUserFormValidator socialUserFormValidator;
	private final ProviderSignInUtils providerSignInUtils;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Inject
	private ConnectionRepository connectionRepository;

	@Autowired
	public UserController(UserService userService, UserCreateFormValidator userCreateFormValidator,
			SocialUserFormValidator socialUserFormValidator, ProviderSignInUtils providerSignInUtils,
			CurrentUserDetailsService currentUserDetailsService) {
		this.userService = userService;
		this.userCreateFormValidator = userCreateFormValidator;
		this.socialUserFormValidator = socialUserFormValidator;
		this.providerSignInUtils = providerSignInUtils;
		this.currentUserDetailsService = currentUserDetailsService;
	}

	@InitBinder("userDTO")
	public void initUserBinder(WebDataBinder binder) {
		binder.addValidators(userCreateFormValidator);
	}

	@InitBinder("socialUserDTO")
	public void initSocialUserBinder(WebDataBinder binder) {
		binder.addValidators(socialUserFormValidator);
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registrationForm(@ModelAttribute UserDTO userDTO, HttpServletRequest request) {
		if (request.getUserPrincipal() != null)
			return "redirect:/";
		else
			return REGISTER_VIEW;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult result, WebRequest request,
			RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return REGISTER_VIEW;
		}
		userDTO.setAuthorities(Lists.newArrayList(new Authority("ROLE_USER")));
		userService.create(userDTO);
		redirect.addFlashAttribute("feedbackMessage", "Account successfully created!");
		return "redirect:/";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm(@ModelAttribute SocialUserDTO socialUserDTO, WebRequest request, Model model) {
		if (request.getUserPrincipal() != null)
			return "redirect:/";
		else {
			Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);

			socialUserDTO = createSocialUserDTO(connection);
			model.addAttribute(MODEL_ATTRIBUTE_SOCIALUSER, socialUserDTO);
			return SIGNUP_VIEW;
		}
	}

	private SocialUserDTO createSocialUserDTO(Connection<?> connection) {
		SocialUserDTO dto = new SocialUserDTO();

		if (connection != null) {
			UserProfile socialMediaProfile = connection.fetchUserProfile();
			dto.setEmail(socialMediaProfile.getEmail());
			dto.setFirstName(socialMediaProfile.getFirstName());
			dto.setLastName(socialMediaProfile.getLastName());

			ConnectionKey providerKey = connection.getKey();
			dto.setSignInProvider(SocialMediaService.valueOf(providerKey.getProviderId().toUpperCase()));
		}

		return dto;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Valid @ModelAttribute("socialUserDTO") SocialUserDTO socialUserDTO, BindingResult result,
			WebRequest request, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return SIGNUP_VIEW;
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(socialUserDTO.getUsername());
		userDTO.setFirstName(socialUserDTO.getFirstName());
		userDTO.setLastName(socialUserDTO.getLastName());
		userDTO.setEmail(socialUserDTO.getEmail());
		userDTO.setPassword("something");
		userDTO.setAuthorities(Lists.newArrayList(new Authority("ROLE_USER")));

		userService.create(userDTO);
		providerSignInUtils.doPostSignUp(socialUserDTO.getUsername(), request);
		redirect.addFlashAttribute("feedbackMessage", "Account successfully created!");
		return "redirect:/";
	}

	@PreAuthorize("@userService.canAccessUser(principal, #username)")
	@RequestMapping(value = "/{username}", method = GET)
	public String profilePage(@PathVariable("username") String username, Model model) throws UsernameNotFoundException {
		logger.info("Showing user page for user: {}", username);
//		Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
//		if (connection != null) {
//			User socialMediaProfile = connection.getApi().userOperations().getUserProfile();
//		}

		CurrentUser found = currentUserDetailsService.loadUserByUsername(username);
		model.addAttribute(MODEL_ATTRIBUTE_CURRENTUSER, found);
		return USER_PROFILE_VIEW;
	}

}
