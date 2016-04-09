package com.nixmash.springdata.jpa.common;

import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.model.User;

/**
 * Created by daveburke on 4/9/16.
 */
public class UserUtils {

    public static UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setSignInProvider(user.getSignInProvider());

        return userDTO;
    }

}
