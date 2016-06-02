package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jsoup.dto.PagePreviewDTO;
import com.nixmash.springdata.jsoup.service.JsoupService;
import com.nixmash.springdata.mvc.components.WebUI;
import com.nixmash.springdata.mvc.containers.PostLink;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by daveburke on 5/27/16.
 */
@Controller
@RequestMapping(value = "/posts")
public class PostsController {

    private static final Logger logger = LoggerFactory.getLogger(PostsController.class);

    protected static final String POSTS_LIST_VIEW = "posts/list";
    public static final String POSTS_ADD_VIEW = "posts/add";

    WebUI webUI;
    JsoupService jsoupService;

    @Autowired
    public PostsController(WebUI webUI, JsoupService jsoupService) {
        this.webUI = webUI;
        this.jsoupService = jsoupService;
    }

    @RequestMapping(value = "", method = GET)
    public String home() {
        return POSTS_LIST_VIEW;
    }

    @RequestMapping(value = "/add", method = GET, params = {"formtype"})
    public String displayAddPostForm(@RequestParam(value = "formtype") String formType, PostLink postLink, BindingResult result, Model model) {
        PostType postType = PostType.valueOf(formType.toUpperCase());
        String hasPost = null;

        if (postType.equals(PostType.NOTE)) {
            hasPost = "note";
        } else {
            if (StringUtils.isEmpty(postLink.getLink())) {
                result.rejectValue("link", "post.link.is.empty");
            } else {
                PagePreviewDTO page = jsoupService.getPagePreview(postLink.getLink());
                if (page == null) {
                    result.rejectValue("link", "post.link.page.not.found");
                    return POSTS_ADD_VIEW;
                } else {
                    hasPost = "link";
                }
            }
        }
        model.addAttribute("hasPost", hasPost);
        return POSTS_ADD_VIEW;
    }

    @RequestMapping(value = "/add", method = GET)
    public String addPost(Model model) {
        model.addAttribute("postLink", new PostLink());
        return POSTS_ADD_VIEW;
    }

}