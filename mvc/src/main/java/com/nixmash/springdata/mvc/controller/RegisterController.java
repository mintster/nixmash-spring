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
import com.nixmash.springdata.jpa.model.Authority;
import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.model.validators.UserCreateFormValidator;
import com.nixmash.springdata.jpa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Allows users to sign up.
 *
 * @author Rob Winch
 */
@Controller
@RequestMapping("/register")
public class RegisterController {
    private final UserService userService;
    private final UserCreateFormValidator userCreateFormValidator;

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public RegisterController(UserService userService, UserCreateFormValidator userCreateFormValidator) {
        this.userService = userService;
        this.userCreateFormValidator = userCreateFormValidator;
    }


    @InitBinder("userDTO")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String registrationForm(@ModelAttribute UserDTO userDTO, HttpServletRequest request) {
        if (request.getUserPrincipal() != null)
            return "redirect:/";
        else
            return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult result,
                           RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "register";
        }
        userDTO.setAuthorities(Lists.newArrayList(new Authority("ROLE_USER")));
        try {
            userService.create(userDTO);
        } catch (DataIntegrityViolationException e) {
            logger.warn("Exception occurred when trying to save the user", e);
            result.reject("unknown.error", "An error has occurred and has been logged");
            return "register";
        }
        // Okay User Created
        redirect.addFlashAttribute(ContactController.FLASH_MESSAGE_KEY_FEEDBACK, "Successfully registered");
        return "redirect:/contacts";
    }
}
