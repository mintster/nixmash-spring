package com.nixmash.springdata.jsoup.base;

/**
 * Created by daveburke on 5/22/16.
 */
@SuppressWarnings("WeakerAccess")
public class JsoupLink {

    public String href;
    public String text;

    // region getter setters

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // endregion


    //region toString()

    @Override
    public String toString() {
        return "JsoupLink{" +
                "href='" + href + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    //endregion
}
