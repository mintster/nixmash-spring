package com.nixmash.springdata.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("mysql")
public class BatchContext {

    @Autowired
    private JobRunner jobRunner;

    @Test
    public void contextLoads() {
        assertThat(jobRunner).isNotNull();
    }

}