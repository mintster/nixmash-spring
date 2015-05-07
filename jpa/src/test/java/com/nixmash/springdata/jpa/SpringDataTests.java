package com.nixmash.springdata.jpa;

import com.nixmash.springdata.jpa.config.DevConfiguration;
import com.nixmash.springdata.jpa.config.SpringProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(classes = DevConfiguration.class)
@Transactional
@ActiveProfiles("dev")
public class SpringDataTests {

    @Autowired
    SpringProperties springProperties;

    @Test
    public void contextLoads() {
        assertNotNull(springProperties.getToken());

    }

}

