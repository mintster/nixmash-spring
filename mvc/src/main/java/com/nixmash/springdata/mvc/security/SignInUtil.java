package com.nixmash.springdata.mvc.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.User;

public class SignInUtil {

    public static void authorizeUser(User user) {
    	
        CurrentUser currentUser = new CurrentUser(user);
 		SecurityContextHolder.getContext().setAuthentication(
             new UsernamePasswordAuthenticationToken(currentUser,user.getPassword(), user.getAuthorities()));

    }
    
}
