package com.nixmash.springdata.jsoup;

import com.nixmash.springdata.jsoup.dto.TestDTO;
import com.nixmash.springdata.jsoup.base.JSoupHtmlParser;
import com.nixmash.springdata.jsoup.parsers.TestDTOParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by daveburke on 5/21/16.
 */
@Configuration
public class JsoupTestConfig {

    @Bean
    public JSoupHtmlParser<TestDTO> testDTOParser() {
        return new TestDTOParser(TestDTO.class);
    }
}
