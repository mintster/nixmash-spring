package com.nixmash.springdata.jsoup.components;

import com.nixmash.springdata.jsoup.dto.ParsedDTO;
import com.nixmash.springdata.jsoup.parsers.JSoupHtmlParser;
import com.nixmash.springdata.jsoup.utils.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class JsoupUI {

    @Autowired
    @Qualifier("parsedDTOParser")
    JSoupHtmlParser<ParsedDTO> parsedDTOParser;

    String url = "http://jabbawonk/x/jsoup.html";

    public void init() {
        parseIt();
        jsoupLocalHtml();
    }

    private void jsoupLocalHtml()  {
        File in = JsoupUtil.getFile("/html/jsoup.html");
        Document doc = null;
        try {
            doc = Jsoup.parse(in, null, "http://example.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(doc.toString());
    }

    private void parseIt() {
        ParsedDTO parsedDTO = parsedDTOParser.parse(url);
        System.out.println(parsedDTO.getContents());
    }
}
