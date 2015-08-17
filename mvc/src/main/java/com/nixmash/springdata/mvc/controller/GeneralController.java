package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.mvc.security.CurrentUser;
import com.nixmash.springdata.mvc.security.CurrentUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GeneralController {

    @Autowired
    private CurrentUserDetailsService userDetailsService;

    @Autowired
    private ApplicationSettings applicationSettings;

    @ModelAttribute("currentUser")
    public CurrentUser getCurrentUser(Authentication authentication) {
        if (authentication == null)
            return null;
        else
            return userDetailsService.loadUserByUsername(authentication.getName());
    }

    @ModelAttribute("appSettings")
    public ApplicationSettings getApplicationSettings() {
        return applicationSettings;
    }

}

