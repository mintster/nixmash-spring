package com.nixmash.springdata.jpa.model.validators;

import com.nixmash.springdata.jpa.dto.UserPasswordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserPasswordValidator implements Validator {

    private static final Logger logger = LoggerFactory.getLogger(UserPasswordValidator.class);

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserPasswordDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        logger.debug("Validating {}", target);
        UserPasswordDTO form = (UserPasswordDTO) target;
        validatePasswords(errors, form);
    }

    private void validatePasswords(Errors errors, UserPasswordDTO form) {
        if (!form.getPassword().equals(form.getRepeatedPassword())) {
            errors.reject("password.no_match", "Passwords do not match");
        }
    }
}
