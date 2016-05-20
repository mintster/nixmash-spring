package com.nixmash.springdata.jsoup.components;

import com.nixmash.springdata.jsoup.common.JSoupHtmlParser;
import com.nixmash.springdata.jsoup.common.ParsedDTO;
import com.nixmash.springdata.mail.service.JsoupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsoupUI {

    JsoupService jsoupService;

    @Autowired
    JSoupHtmlParser<ParsedDTO> jSoupHtmlParser;

    @Autowired
    public JsoupUI(JsoupService jsoupService) {
        this.jsoupService = jsoupService;
    }

    public void init() {
//        jsoupDemo();
        parseIt();
    }

    private void jsoupDemo() {
        System.out.println(jsoupService.getDemoJsoupHTML());
    }

    private void parseIt() {
        ParsedDTO parsedDTO = jSoupHtmlParser.parse();
        System.out.println(parsedDTO.getContents());
    }
}
