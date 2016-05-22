package com.nixmash.springdata.jsoup.dto;

import com.nixmash.springdata.jsoup.annotations.*;

/**
 * Created by daveburke on 5/19/16.
 */
public class TestDTO {

    @Selector(".myclass")
    @TextValue
    public String myClassText;

    @Selector(".myclass")
    @AttributeValue(name="myattr")
    public String myClassAttribute;

    @Selector(".myclass")
    @HtmlValue
    public String myClassHtml;

    @Selector("#myid")
    @TextValue
    public String myIdText;

    @MetaName("twitter:image:src")
    public String twitterImage;

    @MetaProperty("og:image")
    public String facebookImage;

    // region getters setters

    public String getMyClassAttribute() {
        return myClassAttribute;
    }

    public String getMyClassHtml() {
        return myClassHtml;
    }

    public String getMyIdText() {
        return myIdText;
    }

    public String getMyClassText() {
        return myClassText;
    }

    public void setMyClassAttribute(String myClassAttribute) {
        this.myClassAttribute = myClassAttribute;
    }

    public void setMyClassHtml(String myClassHtml) {
        this.myClassHtml = myClassHtml;
    }

    public void setMyIdText(String myIdText) {
        this.myIdText = myIdText;
    }

    public void setMyClassText(String myClassText) {
        this.myClassText = myClassText;
    }

    public String getTwitterImage() {
        return twitterImage;
    }

    public void setTwitterImage(String twitterImage) {
        this.twitterImage = twitterImage;
    }

    public String getFacebookImage() {
        return facebookImage;
    }

    public void setFacebookImage(String facebookImage) {
        this.facebookImage = facebookImage;
    }
    // endregion

}
