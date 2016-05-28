package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.mvc.components.WebUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by daveburke on 5/27/16.
 */
@Controller
@RequestMapping("/posts")
public class PostsController {

    private static final Logger logger = LoggerFactory.getLogger(PostsController.class);

    protected static final String POSTS_LIST_VIEW = "posts/list";

    WebUI webUI;

    @Autowired
    public PostsController(WebUI webUI) {
        this.webUI = webUI;
    }

    @RequestMapping(value = "", method = GET)
    public String home() {
        return POSTS_LIST_VIEW;
    }
}
