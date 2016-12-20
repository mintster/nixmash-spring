package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.repository.UserDataRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

/**
 * Created by daveburke on 12/20/16.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Transactional
@ActiveProfiles(DataConfigProfile.H2)
public class UserDataTests {

    @Autowired
    UserService userService;

    @Autowired
    UserDataRepository userDataRepository;

    @Test
    public void userDataIsNotNull() throws Exception {
        // H2Data Tommy loginAttempts = 2
        User tommy = userService.getUserByUsername("tommy");
        assertEquals(tommy.getUserData().getLoginAttempts(), 2);
    }

}
