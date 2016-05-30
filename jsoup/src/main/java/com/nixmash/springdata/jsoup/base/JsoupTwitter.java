package com.nixmash.springdata.jsoup.base;

@SuppressWarnings("WeakerAccess")
public class JsoupTwitter {

    public String twitterCard;
    public String twitterCreator;
    public String twitterUrl;
    public String twitterTitle;
    public String twitterDescription;
    public String twitterImage;

    public JsoupTwitter(String twitterCard, String twitterCreator, String twitterUrl, String twitterTitle, String twitterDescription, String twitterImage) {
        this.twitterCard = twitterCard;
        this.twitterCreator = twitterCreator;
        this.twitterUrl = twitterUrl;
        this.twitterTitle = twitterTitle;
        this.twitterDescription = twitterDescription;
        this.twitterImage = twitterImage;
    }

    // region getters setters

    public String getTwitterCard() {
        return twitterCard;
    }

    public void setTwitterCard(String twitterCard) {
        this.twitterCard = twitterCard;
    }

    public String getTwitterCreator() {
        return twitterCreator;
    }

    public void setTwitterCreator(String twitterCreator) {
        this.twitterCreator = twitterCreator;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getTwitterTitle() {
        return twitterTitle;
    }

    public void setTwitterTitle(String twitterTitle) {
        this.twitterTitle = twitterTitle;
    }

    public String getTwitterDescription() {
        return twitterDescription;
    }

    public void setTwitterDescription(String twitterDescription) {
        this.twitterDescription = twitterDescription;
    }

    public String getTwitterImage() {
        return twitterImage;
    }

    public void setTwitterImage(String twitterImage) {
        this.twitterImage = twitterImage;
    }


    // endregion


    @Override
    public String toString() {
        return "JsoupTwitter{" +
                "twitterCard='" + twitterCard + '\'' +
                ", twitterCreator='" + twitterCreator + '\'' +
                ", twitterUrl='" + twitterUrl + '\'' +
                ", twitterTitle='" + twitterTitle + '\'' +
                ", twitterDescription='" + twitterDescription + '\'' +
                ", twitterImage='" + twitterImage + '\'' +
                '}';
    }
}

/*
    <!-- Twitter Cards Meta By WPDeveloper.net -->
    <meta name="twitter:card" content="summary" />
    <meta name="twitter:site" content="@daveburkevt" />
    <meta name="twitter:creator" content="@daveburkevt" />
    <meta name="twitter:url" content="http://nixmash.com/java/variations-on-json-key-value-pairs-in-spring-mvc/" />
    <meta name="twitter:title" content="Variations on JSON Key-Value Pairs in Spring MVC" />
    <meta name="twitter:description" content="The topic of this post is pretty lightweight. A bit of a lark, really, but Spring Java coders may find it interesting and maybe even useful. We&#039;re looking at the most efficient way of [...]" />
    <meta name="twitter:image" content="http://nixmash.com/x/blog/2016/jsonpair0528c.png" />
    <!-- Twitter Cards Meta By WPDeveloper.net -->
*/