package com.nixmash.springdata.mvc.security;

import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.components.WebUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.nixmash.springdata.mvc.security.CurrentUserDetailsService.USER_IS_DISABLED;

@Component("authenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String GENERIC_AUTHENTICATION_ERROR_KEY = "user.generic.authentication.error";
    private static final String NOT_YET_USER_VERIFIED_ERROR_KEY = "user.not.verified.message";
    private static final String USERNAME = "username";

    private final WebUI webUI;
    private final UserService userService;

    @Autowired
    public CustomAuthenticationFailureHandler(WebUI webUI, UserService userService) {
        this.webUI = webUI;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request,
                                        final HttpServletResponse response, final AuthenticationException exception)
            throws IOException, ServletException {

        setDefaultFailureUrl("/signin?error");

        super.onAuthenticationFailure(request, response, exception);

        String errorMessage = webUI.getMessage(GENERIC_AUTHENTICATION_ERROR_KEY);

        User user = userService.getUserByUsername(request.getParameter(USERNAME));
        if (user != null) {

            String notYetApprovedMessage = webUI.getMessage(NOT_YET_USER_VERIFIED_ERROR_KEY,
                    user.getUsername(), user.getEmail());

            if (exception.getMessage().equalsIgnoreCase((USER_IS_DISABLED))) {
                if (user.getUserData().getApprovedDatetime() == null) errorMessage = notYetApprovedMessage;
            }
        }
        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }
}