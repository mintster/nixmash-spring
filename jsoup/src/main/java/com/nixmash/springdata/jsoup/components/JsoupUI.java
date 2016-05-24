package com.nixmash.springdata.jsoup.components;

import com.nixmash.springdata.jsoup.dto.PagePreviewDTO;
import com.nixmash.springdata.jsoup.parsers.JSoupHtmlParser;
import com.nixmash.springdata.jsoup.utils.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import static com.nixmash.springdata.jsoup.utils.JsoupUtil.trim;

@Component
public class JsoupUI {

    private static final Logger logger = LoggerFactory.getLogger(JsoupUI.class);

    @Autowired
    @Qualifier("pagePreviewParser")
    JSoupHtmlParser<PagePreviewDTO> pagePreviewParser;

    private Document doc;

    public void init() {
        File in = JsoupUtil.getFile("/html/github.html");
        try {
            doc = Jsoup.parse(in, null, "http://example.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
        displayPagePreviewDTO();
//        displayImages();
//        displayImports();
//        displayLInks();
    }

    private void displayPagePreviewDTO() {
        PagePreviewDTO pagePreviewDTO = pagePreviewParser.parse(doc);
        System.out.println("Title: " + pagePreviewDTO.getTitle());
        System.out.println("Twitter Image: " + pagePreviewDTO.getTwitterImage());
        System.out.println("Facebook Image: " + pagePreviewDTO.getFacebookImage());

        System.out.println(pagePreviewDTO.getImages().get(5).src);
        System.out.println(pagePreviewDTO.getAvatar().src);

        System.out.println(pagePreviewDTO.getLinks().get(51).href);
        System.out.println(pagePreviewDTO.getLink().href);

    }

    // region non-used demos

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
                        trim(src.attr("alt"), 60));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }


    // endregion

}
