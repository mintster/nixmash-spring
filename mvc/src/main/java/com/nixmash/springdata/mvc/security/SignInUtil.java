package com.nixmash.springdata.mvc.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.model.UserConnection;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.controller.GlobalController;

public class SignInUtil {

	public static void authorizeUser(User user) {

		CurrentUser currentUser = new CurrentUser(user);
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(currentUser, user.getPassword(), user.getAuthorities()));

	}

	public static void setUserConnection(WebRequest request, String userId, UserService userService) {
		UserConnection userConnection;
		String sessionUserConnectionAttribute = GlobalController.SESSION_ATTRIBUTE_USER_CONNECTION;
		userConnection  = (UserConnection) request.getAttribute(sessionUserConnectionAttribute, RequestAttributes.SCOPE_SESSION);
		
		if (userConnection == null || !userId.equals(userConnection.getUserId())) {
			userConnection = userService.getUserConnectionByUserId(userId);
			request.setAttribute(sessionUserConnectionAttribute, userConnection, request.SCOPE_SESSION);
		}
	}
	
	// @ModelAttribute("currentUserConnection")
	// public UserConnection getUserConnection(HttpServletRequest request, Principal currentUser, Model model) {
	//
	// // SecurityContext ctx = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
	//
	// String userId = currentUser == null ? null : currentUser.getName();
	// String path = request.getRequestURI();
	// HttpSession session = request.getSession();
	//
	// UserConnection connection = null;
	// UserProfile profile = null;
	// String displayName = null;
	// String data = null;
	//
	// // Collect info if the user is logged in, i.e. userId is set
	// if (userId != null) {
	//
	// // Get the current UserConnection from the http session
	// connection = getUserConnection(session, userId);
	//
	// // Get the current UserProfile from the http session
	// profile = getUserProfile(session, userId);
	//
	// // Compile the best display name from the connection and the profile
	// displayName = getDisplayName(connection, profile);
	//
	// // Get user data from persistence storage
	// data = dataDao.getData(userId);
	// }
	//
	// Throwable exception = (Throwable)session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	//
	// // Update the model with the information we collected
	// model.addAttribute("exception", exception == null ? null : exception.getMessage());
	// model.addAttribute("currentUserId", userId);
	// model.addAttribute("currentUserProfile", profile);
	// model.addAttribute("currentUserConnection", connection);
	// model.addAttribute("currentUserDisplayName", displayName);
	// model.addAttribute("currentData", data);
	//
	// if (LOG.isDebugEnabled()) {
	// logInfo(request, model, userId, path, session);
	// }
	// }
}
