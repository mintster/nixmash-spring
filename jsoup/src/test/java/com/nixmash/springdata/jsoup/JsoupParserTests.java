package com.nixmash.springdata.jsoup;

import com.nixmash.springdata.jsoup.dto.ParsedDTO;
import com.nixmash.springdata.jsoup.parsers.JSoupHtmlParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by daveburke on 5/20/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class JsoupParserTests extends JsoupContext {

    @Autowired
    @Qualifier("parsedDTOParser")
    JSoupHtmlParser<ParsedDTO> parsedDTOParser;

    String url = "http://jabbawonk/x/jsoup.html";

    @Test
    public void parseIt() {
        ParsedDTO parsedDTO = parsedDTOParser.parse(url);
        System.out.println(parsedDTO.getContents());
    }

}

