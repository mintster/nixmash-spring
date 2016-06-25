package com.nixmash.springdata.mvc.security;

import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithUserDetails(value = "keith", userDetailsServiceBeanName = "currentUserDetailsService")
public @interface WithPostUser {
}



