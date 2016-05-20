package com.nixmash.springdata.jsoup.configuration;

import com.nixmash.springdata.jsoup.parsers.JSoupHtmlParser;
import com.nixmash.springdata.jsoup.dto.ParsedDTO;
import com.nixmash.springdata.jsoup.parsers.ParsedDTOParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/jsoup.properties")
public class JsoupConfig {

    @Bean
    public JSoupHtmlParser<ParsedDTO> parsedDTOParser() {
        return new ParsedDTOParser(ParsedDTO.class);
    }
}