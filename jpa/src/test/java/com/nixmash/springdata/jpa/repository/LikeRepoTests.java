package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.model.Like;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by daveburke on 5/31/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles(DataConfigProfile.H2)
public class LikeRepoTests {

    @Autowired
    LikeRepository likeRepository;

    @Before
    public void setUp() {
    }

    @Test
    public void getAllLikesByContentType() {
        List<Like> likes = likeRepository.findByContentTypeId(1);
        System.out.println(likes);
    }

    @Test
    public void getAllLikesByUserId() {
        List<Like> likes = likeRepository.findByUserIdAndContentTypeId(3, 1);
        System.out.println(likes);
    }
}
