package com.nixmash.springdata.jpa.utils;

import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.model.User;

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
        userDTO.setAuthorities(user.getAuthorities());
        userDTO.setHasAvatar(user.hasAvatar());
        userDTO.setUserKey(user.getUserKey());
        return userDTO;
    }

}
