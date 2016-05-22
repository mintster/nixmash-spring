package com.nixmash.springdata.jsoup.dto;

import com.nixmash.springdata.jsoup.annotations.MetaName;
import com.nixmash.springdata.jsoup.annotations.MetaProperty;
import com.nixmash.springdata.jsoup.annotations.Selector;

/**
 * Created by daveburke on 5/19/16.
 */
public class PagePreviewDTO {

    @Selector("title")
    public String title;

    @MetaName("twitter:image:src")
    public String twitterImage;

    @MetaProperty("og:image")
    public String facebookImage;

    public String getFacebookImage() {
        return facebookImage;
    }

    public void setFacebookImage(String facebookImage) {
        this.facebookImage = facebookImage;
    }

    public String getTwitterImage() {
        return twitterImage;
    }


    public void setTwitterImage(String twitterImage) {
        this.twitterImage = twitterImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
