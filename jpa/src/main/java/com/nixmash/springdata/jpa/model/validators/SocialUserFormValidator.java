package com.nixmash.springdata.jpa.model.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nixmash.springdata.jpa.dto.SocialUserDTO;
import com.nixmash.springdata.jpa.service.UserService;

@Component
public class SocialUserFormValidator implements Validator {

    private static final Logger logger = LoggerFactory.getLogger(SocialUserFormValidator.class);
    private final UserService userService;

    @Autowired
    public SocialUserFormValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(SocialUserDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        logger.debug("Validating {}", target);
        SocialUserDTO form = (SocialUserDTO) target;
        validateEmail(errors, form);
        validateUsername(errors, form);
    }

    private void validateEmail(Errors errors, SocialUserDTO form) {
        if (userService.getByEmail(form.getEmail()).isPresent()) {
            errors.reject("email.exists", "User with this email already exists");
        }
    }

    private void validateUsername(Errors errors, SocialUserDTO form) {
        if (userService.getUserByUsername(form.getUsername()) != null) {
            errors.reject("user.exists", "User with this username already exists");
        }
    }
}
