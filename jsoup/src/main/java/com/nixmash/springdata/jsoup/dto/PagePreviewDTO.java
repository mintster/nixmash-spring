package com.nixmash.springdata.jsoup.dto;

import com.nixmash.springdata.jsoup.annotations.*;

import java.util.List;

/**
 * Created by daveburke on 5/19/16.
 */
@SuppressWarnings("WeakerAccess")
public class PagePreviewDTO {

    @Selector("title")
    public String title;

    @MetaName("twitter:image:src")
    public String twitterImage;

    @MetaProperty("og:image")
    public String facebookImage;

    @ImageSelector("#readme")
    public List<JsoupImage> images;

    @ImageSelector(".avatar")
    public JsoupImage avatar;

    @LinkSelector(".mylink")
    public JsoupLink link;

    @LinkSelector("#readme")
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

    public JsoupImage getAvatar() {
        return avatar;
    }

    public void setAvatar(JsoupImage avatar) {
        this.avatar = avatar;
    }

    public JsoupLink getLink() {
        return link;
    }

    public void setLink(JsoupLink link) {
        this.link = link;
    }

    public List<JsoupLink> getLinks() {
        return links;
    }

    public void setLinks(List<JsoupLink> links) {
        this.links = links;
    }

    // endregion

}
