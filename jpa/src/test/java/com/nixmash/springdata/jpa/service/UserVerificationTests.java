package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.TestUtil;
import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Transactional
@ActiveProfiles(DataConfigProfile.H2)
public class UserVerificationTests {


    @Autowired
    private UserService userService;

    @Test
    public void userSavedWithEnabledFalse() {
        UserDTO userDTO = TestUtil.createTestUserDTO("user121528", "bumb", "bammer", "user121528@aol.com");
        userDTO.setEnabled(false);
        User user = userService.create(userDTO);
        assertFalse(user.isEnabled());
        assertNotNull(user.getCreatedDatetime());
        assertNull(user.getApprovedDatetime());
    }

    @Test
    public void canRetrieveUserByUserKey() throws Exception {
        //  Scott Schoenberg retrieved for UserKey 'Fx05XbWjPFECJZQP'
        Optional<User> user = userService.getByUserKey("Fx05XbWjPFECJZQP");
        assertThat(user.isPresent(), is(true));
    }

    @Test
    public void nonExistentUserKeyReturnsEmptyUser() throws Exception {
        //  No one retrieved for UserKey '12345'
        Optional<User> user = userService.getByUserKey("12345");
        assertThat(user.isPresent(), is(false));
    }
}
