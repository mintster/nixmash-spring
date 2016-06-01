package com.nixmash.springdata.jsoup.parsers;

import com.nixmash.springdata.jsoup.base.JsoupHtmlParser;
import com.nixmash.springdata.jsoup.dto.TestDTO;

public class TestDTOParser extends JsoupHtmlParser<TestDTO> {

    public TestDTOParser(Class<TestDTO> classModel) {
        super(classModel);
    }

}