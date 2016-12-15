package com.nixmash.springdata.jpa;

import com.google.common.collect.Lists;
import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.enums.SignInProvider;
import com.nixmash.springdata.jpa.model.Authority;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Petri Kainulainen
 */
public class TestUtil {

    public static String createUpdatedString(String value) {
        StringBuilder builder = new StringBuilder();

        builder.append(value);
        builder.append("Updated");

        return builder.toString();
    }

    public static Date date(int year, int month, int date) {
        Calendar working = GregorianCalendar.getInstance();
        working.set(year, month, date, 0, 0, 1);
        return working.getTime();
    }

    public static ZonedDateTime currentZonedDateTime() {
        Calendar now = Calendar.getInstance();
        ZonedDateTime zdt = ZonedDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());
        return zdt;
    }

    // UserDTO as passed to userService.create(UserDTO)
    public static UserDTO createTestUserDTO(String username, String firstName, String lastName, String email) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setUsername(username);
        userDTO.setLastName(lastName);
        userDTO.setPassword("password");
        userDTO.setEmail(email);
        userDTO.setSignInProvider(SignInProvider.SITE);
        userDTO.setAuthorities(Lists.newArrayList(new Authority("ROLE_USER")));
        return userDTO;
    }

}
