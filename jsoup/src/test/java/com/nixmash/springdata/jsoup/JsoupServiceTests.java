package com.nixmash.springdata.jsoup;


import com.nixmash.springdata.jsoup.dto.PagePreviewDTO;
import com.nixmash.springdata.jsoup.service.JsoupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
public class JsoupServiceTests extends JsoupContext {

    @Autowired
    JsoupService jsoupService;

    private static final String REPO_URL = "https://github.com/mintster/spring-data";

//    @Before
//    public void setup() throws IOException {
//    }


    @Test
    public void throwIOExceptionWithBadUrl() throws IOException {
        PagePreviewDTO pagePreviewDTO = jsoupService.getPagePreview("http://bad.url");
        assert (pagePreviewDTO == null);
    }

    @Test
    public void retrievePagePreviewDTOWithValidUrl() {
        assert (jsoupService.getPagePreview(REPO_URL) != null);
    }
}
