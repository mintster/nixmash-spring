package com.nixmash.springdata.jsoup.service;

import com.nixmash.springdata.jsoup.dto.PagePreviewDTO;

/**
 * Created by daveburke on 5/29/16.
 */
public interface JsoupService {
    PagePreviewDTO getPagePreview(String url);
}
