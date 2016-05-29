package com.nixmash.springdata.jsoup.dto;

import com.nixmash.springdata.jsoup.annotations.*;
import com.nixmash.springdata.jsoup.base.JsoupImage;
import com.nixmash.springdata.jsoup.base.JsoupLink;

import java.util.List;


@SuppressWarnings("WeakerAccess")
public class PagePreviewDTO {

    @Selector("title")
    public String title;

    @MetaName("twitter:image:src")
    public String twitterImage;

    @MetaProperty("og:image")
    public String facebookImage;

    @ImageSelector
    public List<JsoupImage> images;

    @LinkSelector
    public List<JsoupLink> links;


    // region getters setters

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

    public List<JsoupImage> getImages() {
        return images;
    }

    public void setImages(List<JsoupImage> images) {
        this.images = images;
    }

    public List<JsoupLink> getLinks() {
        return links;
    }

    public void setLinks(List<JsoupLink> links) {
        this.links = links;
    }

    // endregion

}
