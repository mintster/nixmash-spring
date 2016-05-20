package com.nixmash.springdata.jsoup.parsers;

import com.google.common.base.Preconditions;
import com.nixmash.springdata.jsoup.annotations.AttributeValue;
import com.nixmash.springdata.jsoup.annotations.HtmlValue;
import com.nixmash.springdata.jsoup.annotations.Selector;
import com.nixmash.springdata.jsoup.annotations.TextValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.lang.reflect.Method;

public class JSoupHtmlParser<T> {

    private final Class<T> classModel;

    // Pass in the class Java bean that will contain the mapped data from the HTML source
    public JSoupHtmlParser( final Class<T> classModel) {
        this.classModel = classModel;
    }
    private final static String STRING = "<html><body><div class='myclass' myname='sneezy'>27</div></body></html>";

    // Main method that will translate HTML to object
    public T parse(String url) {
        try {
            final Document doc = Jsoup.connect(url).get();
//            final Document doc = Jsoup.parse(STRING);
            T model = this.classModel.newInstance();

            for (Method m : this.classModel.getMethods()) {
                String value = null;
                // check if Selector annotation is present in any of the methods
                if (m.isAnnotationPresent(Selector.class)) {
                    value = parseValue(doc, m);
                }

                if (value != null) {
                    m.invoke(model, convertValue(value, m));
                }
            }

            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Use Spring's ConversionService to convert the selected value to the type of the parameter in the setter method
    private static final ConversionService conversion = new DefaultConversionService();

    private Object convertValue(final String value, final Method m) {
        Preconditions.checkArgument(m.getParameterTypes().length > 0);
        return conversion.convert(value, m.getParameterTypes()[0]);
    }

    private String parseValue(final Document doc, final Method m) {
        final String selector = m.getAnnotation(Selector.class).value();

        final Elements elems = doc.select(selector);

        if (elems.size() > 0) {
            final Element elem = elems.get(0);

            // Check which value annotation is present and retrieve data depending on the type of annotation
            if (m.isAnnotationPresent(TextValue.class)) {
                return elem.text();
            } else if (m.isAnnotationPresent(HtmlValue.class)) {
                return elem.html();
            } else if (m.isAnnotationPresent(AttributeValue.class)) {
                return elem.attr(m.getAnnotation(AttributeValue.class).name());
            }
        }

        return null;
    }
}