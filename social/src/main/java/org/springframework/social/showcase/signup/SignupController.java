/*
 * Copyright 2014 the original author or authors.
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
package org.springframework.social.showcase.signup;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.showcase.account.Account;
import org.springframework.social.showcase.message.Message;
import org.springframework.social.showcase.message.MessageType;
import org.springframework.social.showcase.signin.SignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.google.common.collect.Lists;
import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.model.Authority;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.service.UserService;

@Controller
public class SignupController {

	private final UserService userService;
	private final ProviderSignInUtils providerSignInUtils;
	private final PasswordEncoder passwordEncoder;

	@Inject
	public SignupController(UserService userService, ConnectionFactoryLocator connectionFactoryLocator,
			UsersConnectionRepository connectionRepository, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
		this.passwordEncoder = passwordEncoder;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public SignupForm signupForm(WebRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		if (connection != null) {
			request.setAttribute("message",
					new Message(MessageType.INFO,
							"Your " + StringUtils.capitalize(connection.getKey().getProviderId())
									+ " account is not associated with a Spring Social Showcase account. If you're new, please sign up."),
					RequestAttributes.SCOPE_REQUEST);
			return SignupForm.fromProviderUser(connection.fetchUserProfile());
		} else {
			return new SignupForm();
		}
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request) {
		if (formBinding.hasErrors()) {
			return null;
		}
		UserDTO userDTO = new UserDTO();
		userDTO.setFirstName(form.getFirstName());
		userDTO.setLastName(form.getLastName());
		userDTO.setPassword(passwordEncoder.encode(form.getPassword()));
		userDTO.setAuthorities(Lists.newArrayList(new Authority("ROLE_USER")));
		userDTO.setUsername(form.getUsername());
		userDTO.setEmail("my@emailtwo.com");
		
		 User saved = userService.create(userDTO);
		if (saved != null) {
			SignInUtils.signin(saved.getUsername());
			providerSignInUtils.doPostSignUp(saved.getUsername(), request);
			return "redirect:/";
		}
		return null;
	}

	// internal helpers

	private Account createAccount(SignupForm form, BindingResult formBinding) {
		Account account = new Account(form.getUsername(), form.getPassword(), form.getFirstName(),
				form.getLastName());
		UserDTO userDTO = new UserDTO();
		userDTO.setFirstName(form.getFirstName());
		userDTO.setLastName(form.getLastName());
		userDTO.setPassword(passwordEncoder.encode(form.getPassword()));
		userDTO.setAuthorities(Lists.newArrayList(new Authority("ROLE_USER")));
		userDTO.setUsername(form.getUsername());
		userDTO.setEmail("my@emailtwo.com");
		
		 User saved = userService.create(userDTO);
		return account;
	}

}
