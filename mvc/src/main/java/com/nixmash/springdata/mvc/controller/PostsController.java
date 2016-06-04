package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.enums.PostDisplayType;
import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.utils.PostUtils;
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
import org.springframework.web.util.WebUtils;

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
        String showPost;

        if (postType.equals(PostType.NOTE)) {
            showPost = "note";
            model.addAttribute("postDTO", new PostDTO());
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
                    model.addAttribute("postDTO", postDtoFromPagePreview(pagePreview, postLink.getLink()));
                }
            }
        }

        model.addAttribute("showPost", showPost);
        return POSTS_ADD_VIEW;
    }

    private PostDTO postDtoFromPagePreview(PagePreviewDTO page, String sourceLink) {

        Boolean hasTwitter = page.getTwitterDTO() != null;
        String postTitle = hasTwitter ? page.getTwitterDTO().getTwitterTitle() : page.getTitle();
        String postDescription = hasTwitter ? page.getTwitterDTO().getTwitterDescription() : page.getDescription();
        PostDTO tmpDTO = getPagePreviewImage(page, sourceLink);
        // TODO: Add logic for determining if PostDTO hasImages for display of slideshow

        return PostDTO.getBuilder(null,
                postTitle,
                null,
                sourceLink,
                postDescription,
                null,
                null)
                .postImage(tmpDTO.getPostImage())
                .hasImages(tmpDTO.getHasImages())
                .build();
    }

    private PostDTO getPagePreviewImage(PagePreviewDTO page, String sourceLink) {
        return getPagePreviewImage(page, sourceLink, null);
    }

    private PostDTO getPagePreviewImage(PagePreviewDTO page, String sourceLink, Integer imageIndex) {
        String postSource = PostUtils.getPostSource(sourceLink);
        PostDTO tmpDTO = new PostDTO();
        String imageUrl = null;
        Boolean hasImages = true;

        if (imageIndex == null) {

            if (page.twitterDTO.getTwitterImage() != null) {
                imageUrl = page.getTwitterDTO().getTwitterImage();
            } else {
                if (page.getImages().size() > 0) {
                    imageUrl = page.getImages().get(0).getSrc();
                }
                else
                    hasImages = false;
            }
        }
        else
        {
            imageUrl = page.getImages().get(imageIndex).getSrc();
        }

        // anticipating other special providers -- single case for now

        switch (postSource.toLowerCase()) {
            case "stackoverflow.com":
                imageUrl = "/images/stackoverflow.png";
                hasImages = false;
                break;
            default:
                break;
        }

        tmpDTO.setPostImage(imageUrl);
        tmpDTO.setHasImages(hasImages);

        return tmpDTO;
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
    public String createLink(@Valid PostDTO postDTO, Integer imageIndex, BindingResult result, SessionStatus status,
                             RedirectAttributes attributes, Model model, HttpServletRequest request) {
        PagePreviewDTO pagePreview = (PagePreviewDTO) WebUtils.getSessionAttribute(request, "pagePreview");
        if (result.hasErrors()) {
            model.addAttribute("pagePreview", pagePreview);
            model.addAttribute("showPost", "link");
            return POSTS_ADD_VIEW;
        } else {
            if (postDTO.getHasImages()) {
                if (postDTO.getDisplayType() != PostDisplayType.LINK) {
                    postDTO.setPostImage(pagePreview.getImages().get(imageIndex).src);
                }
            }
            webUI.addFeedbackMessage(attributes, FEEDBACK_POST_LINK_ADDED);
            return "redirect:/posts";
        }
    }

}