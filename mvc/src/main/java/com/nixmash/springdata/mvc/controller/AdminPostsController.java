package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.jpa.utils.PostUtils;
import com.nixmash.springdata.jsoup.dto.PagePreviewDTO;
import com.nixmash.springdata.jsoup.service.JsoupService;
import com.nixmash.springdata.mail.service.TemplateService;
import com.nixmash.springdata.mvc.components.WebUI;
import com.nixmash.springdata.mvc.containers.PostLink;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by daveburke on 8/29/16.
 */
@Controller
@RequestMapping(value = "/admin/posts")
public class AdminPostsController {

    public static final String ADMIN_POSTS_LIST_VIEW = "admin/posts/list";
    public static final String ADMIN_POST_ADD_VIEW = "admin/posts/addpost";
    public static final String ADMIN_LINK_ADD_VIEW = "admin/posts/addlink";
    public static final String ADMIN_POSTLINK_UPDATE_VIEW = "admin/posts/update";

    private static final String MESSAGE_ADMIN_UPDATE_POSTLINK_TITLE = "admin.update.postlink.title";
    private static final String MESSAGE_ADMIN_UPDATE_POSTLINK_HEADING = "admin.update.postlink.heading";
    private static final String ADD_POST_HEADER = "posts.add.note.page.header";
    private static final String ADD_LINK_HEADER = "posts.add.link.page.header";

    private static final String SESSION_ATTRIBUTE_NEWPOST = "activepostdto";

    private final PostService postService;
    private final WebUI webUI;
    private final TemplateService templateService;
    private final JsoupService jsoupService;

    private static final Logger logger = LoggerFactory.getLogger(AdminPostsController.class);

    @Autowired
    public AdminPostsController(PostService postService, WebUI webUI, TemplateService templateService, JsoupService jsoupService) {
        this.postService = postService;
        this.webUI = webUI;
        this.templateService = templateService;
        this.jsoupService = jsoupService;
    }

    @RequestMapping(value = "", method = GET)
    public ModelAndView postsListPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("posts", postService.getAllPosts());
        mav.setViewName(ADMIN_POSTS_LIST_VIEW);
        return mav;
    }

    @RequestMapping(value = "/add/{type}", method = GET)
    public String addPostLink(@PathVariable("type") String type, Model model, HttpServletRequest request) {
        PostType postType = PostType.valueOf(type.toUpperCase());
        model.addAttribute("postDTO", new PostDTO());
        if (postType == PostType.POST) {
            WebUtils.setSessionAttribute(request, SESSION_ATTRIBUTE_NEWPOST, null);
            model.addAttribute("postheader", webUI.getMessage(ADD_POST_HEADER));
            return ADMIN_POST_ADD_VIEW;
        } else {
            model.addAttribute("postLink", new PostLink());
            model.addAttribute("postheader", webUI.getMessage(ADD_LINK_HEADER));
            return ADMIN_LINK_ADD_VIEW;
        }
    }

    @RequestMapping(value = "/add/link", params = {"isUrl"}, method = GET)
    public String addLink(@RequestParam(value = "isUrl") Boolean isUrl,
                          @Valid PostLink postLink, BindingResult result, Model model, HttpServletRequest request) {
        model.addAttribute("postheader", webUI.getMessage(ADD_LINK_HEADER));
        if (StringUtils.isEmpty(postLink.getLink())) {
            result.rejectValue("link", "post.link.is.empty");
        } else {
            PagePreviewDTO pagePreview = jsoupService.getPagePreview(postLink.getLink());
            if (pagePreview == null) {
                result.rejectValue("link", "post.link.page.not.found");
                return ADMIN_LINK_ADD_VIEW;
            } else {
                model.addAttribute("hasLink", true);
                WebUtils.setSessionAttribute(request, "pagePreview", pagePreview);
                model.addAttribute("pagePreview", pagePreview);
                model.addAttribute("postDTO",
                        postDtoFromPagePreview(pagePreview, postLink.getLink()));
            }
        }
        return ADMIN_LINK_ADD_VIEW;
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = "/update/{postId}", method = GET)
    public String updatePost(@PathVariable("postId") Long postId,
                             Model model) throws PostNotFoundException {
        Post post = postService.getPostById(postId);
        String postType = StringUtils.capitalize(post.getPostType().name());
        String pageTitle = webUI.getMessage(MESSAGE_ADMIN_UPDATE_POSTLINK_TITLE, postType);
        String pageHeading = webUI.getMessage(MESSAGE_ADMIN_UPDATE_POSTLINK_HEADING, postType);

        model.addAttribute("postDTO", getUpdatedPostDTO(post));
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageHeading", pageHeading);
        model.addAttribute("fileuploading", templateService.getFileUploadingScript());
        model.addAttribute("fileuploaded", templateService.getFileUploadedScript());
        return ADMIN_POSTLINK_UPDATE_VIEW;
    }


    // region postDTO Utilities

    private PostDTO getUpdatedPostDTO(Post post) {
        return PostDTO.getUpdateFields(post.getPostId(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getDisplayType())
                .tags(PostUtils.tagsToTagDTOs(post.getTags()))
                .build();
    }

    private PostDTO postDtoFromPagePreview(PagePreviewDTO page, String postLink) {

        Boolean hasTwitter = page.getTwitterDTO() != null;
        String postTitle = hasTwitter ? page.getTwitterDTO().getTwitterTitle() : page.getTitle();
        String postDescription = hasTwitter ? page.getTwitterDTO().getTwitterDescription() : page.getDescription();
        PostDTO tmpDTO = getPagePreviewImage(page, postLink);

        // If twitter metatags missing title and description but have card metatag

        if (postTitle == null)
            postTitle = page.getTitle();
        if (postDescription == null)
            postDescription = page.getDescription();

        String postDescriptionHtml = null;
        if (StringUtils.isNotEmpty(postDescription))
            postDescriptionHtml = String.format("<p>%s</p>", postDescription);

        return PostDTO.getBuilder(null,
                postTitle,
                null,
                postLink,
                postDescriptionHtml,
                PostType.LINK,
                null)
                .postImage(tmpDTO.getPostImage())
                .hasImages(tmpDTO.getHasImages())
                .build();
    }

    private PostDTO getPagePreviewImage(PagePreviewDTO page, String sourceLink) {
        return getPagePreviewImage(page, sourceLink, null);
    }

    private PostDTO getPagePreviewImage(PagePreviewDTO page, String sourceLink, Integer imageIndex) {
        String postSource = PostUtils.createPostSource(sourceLink);
        PostDTO tmpDTO = new PostDTO();
        String imageUrl = null;
        Boolean hasImages = true;

        if (imageIndex == null) {

            // populating the postDTO image contents for addLink form

            if (page.twitterDTO != null) {
                imageUrl = page.getTwitterDTO().getTwitterImage();
                if (imageUrl != null) {
                    if (!StringUtils.startsWithIgnoreCase(imageUrl, "http"))
                        imageUrl = null;
                }
            } else {
                if (page.getImages().size() > 1) {
                    imageUrl = page.getImages().get(1).getSrc();
                } else
                    hasImages = false;
            }
            // if twitter image url missing or page contains single image

            if (StringUtils.isEmpty(imageUrl)) {
                hasImages = false;
                imageUrl = null;
            }
        } else {
            // determining the final postDTO from addLink form carousel index

            imageUrl = page.getImages().get(imageIndex).getSrc();
        }

        // At some future point may require a database lookup approach:
        // if getNoImageSources(postSource) != null, imageUrl = "/images...{postSource}.png"

        switch (postSource.toLowerCase()) {
            case "stackoverflow.com":
                imageUrl = "/images/posts/stackoverflow.png";
                hasImages = false;
                break;
            case "spring.io":
            case "docs.spring.io":
                imageUrl = "/images/posts/spring.png";
                hasImages = false;
                break;
            case "github.com":
                imageUrl = "/images/posts/github.png";
                hasImages = false;
                break;
            default:
                break;
        }

        tmpDTO.setPostImage(imageUrl);
        tmpDTO.setHasImages(hasImages);

        return tmpDTO;
    }

    private String cleanContentTailHtml(String content) {
        String[] tags = {"<p>\r\n</p>", "<p></p>", "<p><br></p>", "<br>"};
        String result = content;
        for (String t :
                tags) {
            result = StringUtils.removeEnd(result, t);
        }
        return result;

    }

    private Boolean isDuplicatePost(PostDTO postDTO, Post sessionPost) {
        Boolean isDuplicate = false;

        if (StringUtils.isNotEmpty(postDTO.getPostTitle())) {
            String slug = PostUtils.createSlug(postDTO.getPostTitle());
            Post found = null;
            try {
                found = postService.getPost(slug);
            } catch (PostNotFoundException e) {}
            if (sessionPost != null) {
                if (found != null && !(found.getPostId().equals(sessionPost.getPostId()))) {
                    isDuplicate = true;
                }
            } else {
                if (found != null)
                    isDuplicate = true;
            }
        }
        return isDuplicate;
    }

    // endregion

}
