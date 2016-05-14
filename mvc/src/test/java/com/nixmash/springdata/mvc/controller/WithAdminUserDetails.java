package com.nixmash.springdata.mvc.controller;

import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithUserDetails(value = "admin", userDetailsServiceBeanName = "currentUserDetailsService")
public @interface WithAdminUserDetails {
}