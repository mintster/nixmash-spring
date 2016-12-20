package com.nixmash.springdata.jpa.utils;

import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Calendar;
import java.util.Date;

public class UserUtils {

    public static UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setRepeatedPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setSignInProvider(user.getSignInProvider());
        userDTO.setAuthorities(user.getAuthorities());
        userDTO.setHasAvatar(user.hasAvatar());
        userDTO.setUserKey(user.getUserKey());
        return userDTO;
    }

    public static String bcryptedPassword(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }

    public static UserData newRegisteredUserData(User user) {
        Date now = Calendar.getInstance().getTime();
        UserData userData = new UserData();
        userData.setUserId(user.getId());
        userData.setCreatedDatetime(now);
        userData.setLastloginDatetime(now);
        userData.setLoginAttempts(0);
        return userData;
    }
}
