package com.nixmash.springdata.jsoup.service;

import com.nixmash.springdata.jsoup.base.JSoupHtmlParser;
import com.nixmash.springdata.jsoup.dto.PagePreviewDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsoupServiceImpl implements JsoupService {

    private static final Logger logger = LoggerFactory.getLogger(JsoupServiceImpl.class);

    @Autowired
    @Qualifier("pagePreviewParser")
    JSoupHtmlParser<PagePreviewDTO> pagePreviewParser;

    @Override
    public PagePreviewDTO getPagePreview(String url) {
        PagePreviewDTO pagePreviewDTO;
        try {
            Document doc =  Jsoup.connect(url).get();
            pagePreviewDTO = pagePreviewParser.parse(doc);
        } catch (IOException e) {
            logger.error(String.format("Jsoup IOException for url [%s] : %s", url, e.getMessage()));
            return null;
        }
        return pagePreviewDTO;
    }

}
