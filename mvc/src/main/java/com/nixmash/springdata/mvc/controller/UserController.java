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

import com.google.common.collect.Lists;
import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.model.Authority;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.validators.UserCreateFormValidator;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.security.CurrentUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Allows users to sign up.
 *
 * @author Rob Winch
 */
@Controller
public class UserController {


    public static final String MODEL_ATTRIBUTE_CURRENTUSER = "currentUser";
    public static final String USER_PROFILE_VIEW = "users/profile";
    public static final String REGISTER_VIEW = "register";
    public static final String LOGIN_VIEW = "login";

    private final UserService userService;
    private final CurrentUserDetailsService currentUserDetailsService;
    private final UserCreateFormValidator userCreateFormValidator;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService,
                          UserCreateFormValidator userCreateFormValidator,
                          CurrentUserDetailsService currentUserDetailsService) {
        this.userService = userService;
        this.userCreateFormValidator = userCreateFormValidator;
        this.currentUserDetailsService = currentUserDetailsService;
    }


    @InitBinder("userDTO")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registrationForm(@ModelAttribute UserDTO userDTO, HttpServletRequest request) {
        if (request.getUserPrincipal() != null)
            return "redirect:/";
        else
            return REGISTER_VIEW;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult result,
                           RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return REGISTER_VIEW;
        }
        userDTO.setAuthorities(Lists.newArrayList(new Authority("ROLE_USER")));
        userService.create(userDTO);
        redirect.addFlashAttribute(ContactController.FLASH_MESSAGE_KEY_FEEDBACK, "Successfully registered");
        return "redirect:/contacts";
    }

    @PreAuthorize("@userService.canAccessUser(principal, #username)")
    @RequestMapping(value = "/{username}", method = GET)
    public String profilePage(@PathVariable("username")
                                         String username, Model model)
            throws UsernameNotFoundException {
        logger.info("Showing user page for user: {}", username);
        CurrentUser found = currentUserDetailsService.loadUserByUsername(username);
        model.addAttribute(MODEL_ATTRIBUTE_CURRENTUSER, found);
        return USER_PROFILE_VIEW;
    }

    @RequestMapping(value = "/login", method = GET)
    public String login(HttpServletRequest request, Model model) {
        if (request.getUserPrincipal() != null)
            return "redirect:/contacts";
        else
            return LOGIN_VIEW;
    }
}
