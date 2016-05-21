package com.nixmash.springdata.jsoup.dto;

import com.nixmash.springdata.jsoup.annotations.Selector;
import com.nixmash.springdata.jsoup.annotations.TextValue;

/**
 * Created by daveburke on 5/19/16.
 */
public class PagePreviewDTO {

    private String title;

    public String getTitle() {
        return title;
    }

    @Selector("title")
    @TextValue
    public void setTitle(String title) {
        this.title = title;
    }

}
