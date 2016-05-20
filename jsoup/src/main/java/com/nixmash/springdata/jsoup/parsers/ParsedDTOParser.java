package com.nixmash.springdata.jsoup.parsers;

import com.nixmash.springdata.jsoup.dto.ParsedDTO;

public class ParsedDTOParser extends JSoupHtmlParser<ParsedDTO> {

    public ParsedDTOParser(Class<ParsedDTO> classModel) {
        super(classModel);
    }

}