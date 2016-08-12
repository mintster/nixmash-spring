package com.nixmash.springdata.jpa.model.validators;

import com.nixmash.springdata.jpa.dto.UserPasswordDTO;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserPasswordValidator implements Validator {

    private static final Logger logger = LoggerFactory.getLogger(UserPasswordValidator.class);
    private final UserService userService;

    @Autowired
    public UserPasswordValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserPasswordDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        logger.debug("Validating {}", target);
        UserPasswordDTO form = (UserPasswordDTO) target;
        validatePasswords(errors, form);
        preventDemoUserUpdate(errors, form);
    }

    private void preventDemoUserUpdate(Errors errors, UserPasswordDTO form) {
        Optional<User> user = userService.getUserById(form.getUserId());
        if (user.isPresent()) {
            if (user.get().getUsername().toLowerCase().equals("user")) {
                errors.reject("global.error.password.demo.user");
            }
        }
    }

    private void validatePasswords(Errors errors, UserPasswordDTO form) {
        if (!form.getPassword().equals(form.getRepeatedPassword())) {
            errors.reject("password.no_match", "Passwords do not match");
        }
    }
}
