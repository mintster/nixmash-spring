package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.dto.PostDTO;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by daveburke on 5/27/16.
 */
@Controller
@RequestMapping(value = "/posts")
public class PostsController {

    private static final Logger logger = LoggerFactory.getLogger(PostsController.class);

    protected static final String POSTS_LIST_VIEW = "posts/list";
    public static final String POSTS_ADD_VIEW = "posts/add";
    private static final String FEEDBACK_POST_LINK_ADDED = "feedback.post.link.added";
    private static final String FEEDBACK_POST_NOTE_ADDED = "feedback.post.note.added";

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
    public String displayAddPostForm(@RequestParam(value = "formtype") String formType,
                                     PostLink postLink, BindingResult result, Model model, HttpServletRequest request) {
        PostType postType = PostType.valueOf(formType.toUpperCase());
        String showPost = null;

        if (postType.equals(PostType.NOTE)) {
            showPost = "note";
        } else {
            if (StringUtils.isEmpty(postLink.getLink())) {
                result.rejectValue("link", "post.link.is.empty");
                return POSTS_ADD_VIEW;
            } else {
                PagePreviewDTO pagePreview = jsoupService.getPagePreview(postLink.getLink());
                if (pagePreview == null) {
                    result.rejectValue("link", "post.link.page.not.found");
                    return POSTS_ADD_VIEW;
                } else {
                    showPost = "link";
                    request.getSession().setAttribute("pagePreview", pagePreview);
                    model.addAttribute("pagePreview", pagePreview);
                }
            }
        }
        model.addAttribute("postDTO", new PostDTO());
        model.addAttribute("showPost", showPost);
        return POSTS_ADD_VIEW;
    }

    @RequestMapping(value = "/add", method = GET)
    public String addPost(Model model) {
        model.addAttribute("postLink", new PostLink());
        return POSTS_ADD_VIEW;
    }

    @RequestMapping(value = "/add", method = POST, params = {"note"})
    public String createNote(@Valid PostDTO postDTO, BindingResult result, SessionStatus status,
                             RedirectAttributes attributes) {
        webUI.addFeedbackMessage(attributes, FEEDBACK_POST_NOTE_ADDED);
        return "redirect:/posts";
    }


    @RequestMapping(value = "/add", method = POST, params = {"link"})
    public String createLink(@Valid PostDTO postDTO, BindingResult result, SessionStatus status,
                             RedirectAttributes attributes, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("pagePreview", request.getSession().getAttribute("pagePreview"));
            model.addAttribute("showPost", "link");
            return POSTS_ADD_VIEW;
        } else {
            webUI.addFeedbackMessage(attributes, FEEDBACK_POST_LINK_ADDED);
            return "redirect:/posts";
        }
    }

}