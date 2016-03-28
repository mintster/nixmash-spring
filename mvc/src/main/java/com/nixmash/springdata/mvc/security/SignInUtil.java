package com.nixmash.springdata.mvc.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.ConnectionData;
import org.springframework.web.context.request.WebRequest;

import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.mvc.controller.GlobalController;

public class SignInUtil {

	public static void authorizeUser(User user) {

		CurrentUser currentUser = new CurrentUser(user);
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(currentUser, user.getPassword(), user.getAuthorities()));

	}

	public static void setUserConnection(WebRequest request, ConnectionData userConnection) {
		String sessionUserConnectionAttribute = GlobalController.SESSION_ATTRIBUTE_USER_CONNECTION;
		request.setAttribute(sessionUserConnectionAttribute, userConnection, request.SCOPE_SESSION);
	}

}
