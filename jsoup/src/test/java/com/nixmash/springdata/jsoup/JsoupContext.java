package com.nixmash.springdata.jsoup;

import com.nixmash.springdata.jpa.JpaLauncher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes= { JsoupLauncher.class, JpaLauncher.class, JsoupTestConfig.class },
        loader=AnnotationConfigContextLoader.class)
@ActiveProfiles("h2")
public class JsoupContext {

    @Test
    public void contextLoads() {
    }
}
