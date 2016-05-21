package com.nixmash.springdata.jsoup.components;

import com.nixmash.springdata.jsoup.dto.PagePreviewDTO;
import com.nixmash.springdata.jsoup.parsers.JSoupHtmlParser;
import com.nixmash.springdata.jsoup.utils.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class JsoupUI {

    @Autowired
    @Qualifier("pagePreviewParser")
    JSoupHtmlParser<PagePreviewDTO> pagePreviewParser;

    File in;
    String html;
    Document doc;

    public void init() {
        in = JsoupUtil.getFile("/html/github.html");
        try {
            doc = Jsoup.parse(in, null, "http://example.com");
            html = doc.outerHtml();
        } catch (IOException e) {
            e.printStackTrace();
        }
        displayTitleElement();
        displayImages();
    }

    private void displayImports() {
        Elements imports = doc.select("link[href]");
        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel"));
        }

    }

    private void displayLInks() {
        Elements links = doc.select("a[href]");
        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
    }

    private void displayImages() {
        Elements media = doc.select("[src]");

        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

    }

    private void displayTitleElement() {
        PagePreviewDTO pagePreviewDTO = pagePreviewParser.parse(html);
        System.out.println(pagePreviewDTO.getTitle());
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }

}
