package com.nixmash.springdata.jsoup.dto;

import com.nixmash.springdata.jsoup.annotations.AttributeValue;
import com.nixmash.springdata.jsoup.annotations.Selector;

/**
 * Created by daveburke on 5/19/16.
 */
public class ParsedDTO {

    private String contents;

    public String getContents() {
        return contents;
    }

    @Selector(".myclass")
    @AttributeValue(name="myname")
    public void setContents(String contents) {
        this.contents = contents;
    }
}
