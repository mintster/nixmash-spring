package com.nixmash.springdata.jsoup.components;

import com.nixmash.springdata.jsoup.parsers.JSoupHtmlParser;
import com.nixmash.springdata.jsoup.dto.ParsedDTO;
import com.nixmash.springdata.mail.service.JsoupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JsoupUI {

    JsoupService jsoupService;

    @Autowired
    @Qualifier("parsedDTOParser")
    JSoupHtmlParser<ParsedDTO> parsedDTOParser;

    String url = "http://jabbawonk/x/jsoup.html";

    @Autowired
    public JsoupUI(JsoupService jsoupService) {
        this.jsoupService = jsoupService;
    }

    public void init() {
        jsoupDemo();
        parseIt();
    }

    private void jsoupDemo() {
        System.out.println(jsoupService.getDemoJsoupHTML());
    }

    private void parseIt() {
        ParsedDTO parsedDTO = parsedDTOParser.parse(url);
        System.out.println(parsedDTO.getContents());
    }
}
