package com.nixmash.springdata.jsoup.dto;

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

//    for (Element link : links) {
//        print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
//    }
}
