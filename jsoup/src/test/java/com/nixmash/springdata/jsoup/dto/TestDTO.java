package com.nixmash.springdata.jsoup.dto;

import com.nixmash.springdata.jsoup.annotations.AttributeValue;
import com.nixmash.springdata.jsoup.annotations.HtmlValue;
import com.nixmash.springdata.jsoup.annotations.Selector;
import com.nixmash.springdata.jsoup.annotations.TextValue;

/**
 * Created by daveburke on 5/19/16.
 */
public class TestDTO {

    private String myClassText;
    private String myClassAttribute;
    private String myClassHtml;
    private String myIdText;

    // region getters

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

    // endregion

    @Selector(".myclass")
    @AttributeValue(name="myattr")
    public void setMyClassAttribute(String myClassAttribute) {
        this.myClassAttribute = myClassAttribute;
    }

    @Selector(".myclass")
    @HtmlValue
    public void setMyClassHtml(String myClassHtml) {
        this.myClassHtml = myClassHtml;
    }

    @Selector("#myid")
    @TextValue
    public void setMyIdText(String myIdText) {
        this.myIdText = myIdText;
    }

    @Selector(".myclass")
    @TextValue
    public void setMyClassText(String myClassText) {
        this.myClassText = myClassText;
    }


}
