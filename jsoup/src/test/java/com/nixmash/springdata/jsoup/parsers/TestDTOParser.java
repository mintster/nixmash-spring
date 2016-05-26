package com.nixmash.springdata.jsoup.parsers;

import com.nixmash.springdata.jsoup.base.JSoupHtmlParser;
import com.nixmash.springdata.jsoup.dto.TestDTO;

public class TestDTOParser extends JSoupHtmlParser<TestDTO> {

    public TestDTOParser(Class<TestDTO> classModel) {
        super(classModel);
    }

}