package com.nixmash.springdata.jsoup.components;

import com.nixmash.springdata.mail.service.JsoupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsoupUI {

    @Autowired
    JsoupService jsoupService;

    public void init() {
        jsoupDemo();
    }

    private void jsoupDemo() {
        System.out.println(jsoupService.getDemoJsoupHTML());
    }
}
