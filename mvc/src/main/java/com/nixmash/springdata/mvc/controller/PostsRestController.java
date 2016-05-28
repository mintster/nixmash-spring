package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by daveburke on 5/27/16.
 */
@RestController
@RequestMapping(value = "/json/posts",  produces = APPLICATION_JSON_VALUE, method = RequestMethod.GET)
public class PostsRestController {

    private static final Logger logger = LoggerFactory.getLogger(PostsRestController.class);

    @RequestMapping(value = "/map")
    public Map<String, String> returnMap() {
        Map<String, String> keyvalues = new HashMap<>();
        keyvalues.put("keyone", "one value");
        keyvalues.put("keytwo", "two value");
        return keyvalues;
    }

    @RequestMapping(value ="/pair")
    public Pair<String, String> returnPair() {
        return new Pair<>("key", "some value");
    }

    @RequestMapping(value="/simpleentry")
    public SimpleEntry<String, String> returnSimpleEntry() throws IOException {
        return new SimpleEntry<>("key", "some value");
    }

    @RequestMapping(value = "/singleton")
    public Map<String, String> returnSingletonMapFromCollection() {
        return  Collections.singletonMap("key", "some value");
    }

}
