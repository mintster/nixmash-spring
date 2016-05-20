package com.nixmash.springdata.jsoup.configuration;

import com.nixmash.springdata.jsoup.common.JSoupHtmlParser;
import com.nixmash.springdata.jsoup.common.ParsedDTO;
import com.nixmash.springdata.jsoup.common.ParsedDTOParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/jsoup.properties")
public class JsoupConfig {

    @Bean
    public JSoupHtmlParser<ParsedDTO> getParsedDTOParser() {
        return new ParsedDTOParser(ParsedDTO.class);
    }
}