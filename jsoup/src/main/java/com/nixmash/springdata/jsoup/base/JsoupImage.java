package com.nixmash.springdata.jsoup.base;

/**
 * Created by daveburke on 5/22/16.
 */
@SuppressWarnings("WeakerAccess")
public class JsoupImage {

    public String src;
    public String alt;
    public Integer height;
    public Integer width;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "JsoupImage{" +
                "src='" + src + '\'' +
                ", alt='" + alt + '\'' +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}
