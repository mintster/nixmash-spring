package com.nixmash.springdata.jpa.utils;

import com.google.common.collect.Lists;
import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.enums.SignInProvider;
import com.nixmash.springdata.jpa.model.Authority;

/**
 * @author Petri Kainulainen
 */
public class UserTestUtils {

    private static final String EMAIL = "testguy%s@aol.com";
    private static final String FIRST_NAME = "Test";
    private static final String LAST_NAME = "Guy";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "testguy%s";

    public static UserDTO newUserDTO(int i, boolean isEnabled) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(FIRST_NAME);
        userDTO.setLastName(LAST_NAME);
        userDTO.setUsername(String.format(USERNAME, i));
        userDTO.setEmail(String.format(EMAIL, i));
        userDTO.setPassword(PASSWORD);
        userDTO.setSignInProvider(SignInProvider.SITE);
        userDTO.setAuthorities(Lists.newArrayList(new Authority("ROLE_USER")));
        userDTO.setEnabled(isEnabled);
        return userDTO;
    }


}
