package com.nixmash.springdata.jsoup.parsers;

import com.nixmash.springdata.jsoup.annotations.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;

public class JSoupHtmlParser<T> {

    private final Class<T> classModel;

    // Pass in the class Java bean that will contain the mapped data from the HTML source
    public JSoupHtmlParser( final Class<T> classModel) {
        this.classModel = classModel;
    }

    // Main method that will translate HTML to object
    public T parse(String html) {
        try {
            final Document doc = Jsoup.parse(html);
            T model = this.classModel.newInstance();

            for (Field f : this.classModel.getDeclaredFields()) {
                String value = null;

                if (f.isAnnotationPresent(Selector.class)) {
                    value = parseSelector(doc, f);
                }

                if (f.isAnnotationPresent(MetaName.class)) {
                    value = parseMetaName(doc, f);
                }

                if (f.isAnnotationPresent(MetaProperty.class)) {
                    value = parseMetaProperty(doc, f);
                }

                if (value != null)
                    f.set(model, value);

            }

            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String parseMetaProperty(Document doc, Field f) {
        String tagproperty = f.getAnnotation(MetaProperty.class).value();

        String selector = String.format("meta[property=%s]", tagproperty);
        Element element = doc.select(selector).first();
        if (element != null)
            return element.attr("content");

        return null;
    }

    private String parseMetaName(Document doc, Field f) {
        String tagname = f.getAnnotation(MetaName.class).value();

        String selector = String.format("meta[name=%s]", tagname);
        Element element = doc.select(selector).first();
        if (element != null)
            return element.attr("content");

        return null;
    }

    private String parseSelector(final Document doc, Field f) {
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
            }
            else
                return elem.text();
        }

        return null;
    }
}