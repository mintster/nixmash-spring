package com.nixmash.springdata.jsoup.base;

import com.nixmash.springdata.jsoup.annotations.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.nixmash.springdata.jsoup.utils.JsoupUtil.attrIntToNull;
import static com.nixmash.springdata.jsoup.utils.JsoupUtil.trim;

public class JSoupHtmlParser<T> {

    private final Class<T> classModel;
    private Document doc;

    // Pass in the class Java bean that will contain the mapped data from the HTML source
    public JSoupHtmlParser(final Class<T> classModel) {
        this.classModel = classModel;
    }

    // Main method that will translate HTML to object
    public T parse(Document doc) {
        try {
            this.doc = doc;
            T model = this.classModel.newInstance();

            for (Field f : this.classModel.getDeclaredFields()) {
                String value = null;

                // selections and meta keys

                if (f.isAnnotationPresent(Selector.class)) {
                    value = parseSelector(f);
                }

                if (f.isAnnotationPresent(MetaName.class)) {
                    value = parseMetaName(f);
                }

                if (f.isAnnotationPresent(MetaProperty.class)) {
                    value = parseMetaProperty(f);
                }

                if (value != null)
                    f.set(model, value);

                // twitter container

                if (f.isAnnotationPresent(TwitterSelector.class)) {
                    JsoupTwitter jsoupTwitter= parseTwitter();
                    if (jsoupTwitter!= null)
                        f.set(model, jsoupTwitter);
                }

                // images

                if (f.isAnnotationPresent(ImageSelector.class)) {
                    Type genericFieldType = f.getGenericType();

                    Boolean isMultiple = false;

                    if (genericFieldType instanceof ParameterizedType) {
                        isMultiple = true;
                    }

                    if (isMultiple) {
                        List<JsoupImage> images = parseMultipleImages(f);
                        if (images != null)
                            f.set(model, images);
                    } else {
                        JsoupImage image = parseImage(f);
                        if (image != null)
                            f.set(model, image);
                    }
                }

                if (f.isAnnotationPresent(LinkSelector.class)) {
                    Type genericFieldType = f.getGenericType();

                    Boolean isMultiple = false;

                    if (genericFieldType instanceof ParameterizedType) {
                        isMultiple = true;
                    }

                    if (isMultiple) {
                        List<JsoupLink> links = parseMultipleLinks(f);
                        if (links != null)
                            f.set(model, links);
                    } else {
                        JsoupLink link = parseLink(f);
                        if (link != null)
                            f.set(model, link);
                    }
                }

            }


            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<JsoupLink> parseMultipleLinks(Field f) {
        List<JsoupLink> links = new ArrayList<>();
        Elements section;
        Elements elements;

        String css = f.getAnnotation(LinkSelector.class).value();
        if (css.length() > 0) {
            section = doc.select(css);
            if (section == null)
                return null;
            elements = section.first().select("a[href]");
        } else {
            elements = doc.select("a[href]");
        }

        for (Element element : elements) {
            if (element.tagName().equals("a")) {
                links.add(createJsoupLink(element));
            }
        }
        return links;

    }

    private JsoupLink parseLink(Field f) {
        String css = f.getAnnotation(LinkSelector.class).value();
        String selector = String.format("a[href]%s", css);
        Element element = doc.select(selector).first();
        if (element != null) {
            return createJsoupLink(element);
        }
        return null;
    }

    private JsoupLink createJsoupLink(Element element) {
        JsoupLink link = new JsoupLink();
        link.setHref(element.attr("abs:href"));
        link.setText(trim(element.text(), 120));
        return link;
    }


    private JsoupTwitter parseTwitter() {

        if (doc.select("meta[name=twitter:card]").first() != null) {
            return new JsoupTwitter(
                    twitterContent("card"),
                    twitterContent("creator"),
                    twitterContent("url"),
                    twitterContent("title"),
                    twitterContent("description"),
                    twitterContent("image")
            );
        } else
            return null;
    }

    private String twitterContent(String tagName) {
        String selector = String.format("meta[name=twitter:%s]", tagName);
        String content = null;
        Element element = doc.select(selector).first();
        if (element != null) {
            content = element.attr("content");
        }
        return content;
    }

    private JsoupImage parseImage(Field f) {
        String css = f.getAnnotation(ImageSelector.class).value();
        String selector = String.format("img%s", css);
        Element media = doc.select(selector).first();
        if (media != null) {
            return createImageElement(media);
        }
        return null;
    }

    private List<JsoupImage> parseMultipleImages(Field f) {
        List<JsoupImage> images = new ArrayList<>();
        Elements section;
        Elements elements;

        String css = f.getAnnotation(ImageSelector.class).value();
        if (css.length() > 0) {
            section = doc.select(css);
            if (section == null)
                return null;
            elements = section.first().select("[src]");
        } else {
            elements = doc.select("[src]");
        }

        for (Element src : elements) {
            if (src.tagName().equals("img")) {
                images.add(createImageElement(src));
            }
        }
        return images;
    }


    private JsoupImage createImageElement(Element media) {
        JsoupImage img = new JsoupImage();
        img.setSrc(media.attr("abs:src"));
        img.setAlt(trim(media.attr("alt"), 60));
        img.setHeight(attrIntToNull(media.attr("height")));
        img.setWidth(attrIntToNull(media.attr("width")));
        return img;
    }

    private String parseMetaProperty(Field f) {
        String tagproperty = f.getAnnotation(MetaProperty.class).value();

        String selector = String.format("meta[property=%s]", tagproperty);
        Element element = doc.select(selector).first();
        if (element != null)
            return element.attr("content");

        return null;
    }

    private String parseMetaName(Field f) {
        String tagname = f.getAnnotation(MetaName.class).value();

        String selector = String.format("meta[name=%s]", tagname);
        Element element = doc.select(selector).first();
        if (element != null)
            return element.attr("content");

        return null;
    }

    private String parseSelector(Field f) {
        String selector = f.getAnnotation(Selector.class).value();

        Elements elems = doc.select(selector);

        if (elems.size() > 0) {
            final Element elem = elems.get(0);

            // Check which value annotation is present and retrieve data depending on the type of annotation
            if (f.isAnnotationPresent(TextValue.class)) {
                return elem.text();
            } else if (f.isAnnotationPresent(HtmlValue.class)) {
                return elem.html();
            } else if (f.isAnnotationPresent(AttributeValue.class)) {
                return elem.attr(f.getAnnotation(AttributeValue.class).name());
            } else
                return elem.text();
        }

        return null;
    }
}