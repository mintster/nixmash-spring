package com.nixmash.springdata.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("h2")
public class BatchContext {

    @Test
    public void contextLoads() {
    }

}