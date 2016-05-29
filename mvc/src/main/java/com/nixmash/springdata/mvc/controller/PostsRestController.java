package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.utils.Pair;
import com.nixmash.springdata.mvc.annotations.JsonRequestMapping;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")

@RestController
@JsonRequestMapping(value = "/json/posts")
public class PostsRestController {

    private String key = "key";
    private String value = "some value";

    /*
    *           Returns:  {  "key" : "some value"  }
     */
    @RequestMapping(value = "/map", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> returnMap() {
        Map<String, String> keyvalues = new HashMap<>();
        keyvalues.put(key, value);
        return keyvalues;
    }

    /*
    *           Returns:  {  "key" : "some value"  }
     */
    @RequestMapping(value = "/simpleentry")
    public SimpleEntry<String, String> returnSimpleEntry() {
        return new SimpleEntry<>(key, value);
    }

    /*
    *           Returns:  {  "key" : "some value"  }
     */
    @RequestMapping(value = "/singleton")
    public Map<String, String> returnSingletonMapFromCollection() {
        return Collections.singletonMap(key, value);
    }

    /*
    *           Returns:
    *           {
    *                    "key" : "key",
    *                     "value" : "some value"
    *           }
     */
    @RequestMapping(value = "/pair")
    public Pair<String, String> returnPair() {
        return new Pair<>(key, value);
    }

}
