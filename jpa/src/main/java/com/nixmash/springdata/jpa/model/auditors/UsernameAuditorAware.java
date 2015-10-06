package com.nixmash.springdata.jpa.model.auditors;

import com.nixmash.springdata.jpa.model.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This component returns the username of the authenticated user.
 *
 * From Petri Kainulainen's JPA Examples Project on GitHub
 *
 * spring-data-jpa-examples/query-methods/
 * https://goo.gl/lY7sT5
 *
 */
public class UsernameAuditorAware implements AuditorAware<String> {

    private static final Logger logger = LoggerFactory.getLogger(UsernameAuditorAware.class);
    protected static final String ANONYMOUS_USERNAME = "anonymous";
    protected static final String TESTGUY_USERNAME = "testguy";

    @Override
    public String getCurrentAuditor() {
        logger.debug("Getting the username of authenticated user.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
        {
            // in testing mode, return admin
            return TESTGUY_USERNAME;
        }

        if (authentication.getPrincipal().equals("anonymousUser")) {
            logger.debug("Current user is anonymous.");
            return ANONYMOUS_USERNAME;
        }

        String username = ((CurrentUser) authentication.getPrincipal()).getUsername();
        logger.debug("Returning username: {}", username);

        return username;
    }
}
