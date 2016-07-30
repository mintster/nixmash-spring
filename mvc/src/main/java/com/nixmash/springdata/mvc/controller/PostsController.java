package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.enums.PostDisplayType;
import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.enums.Role;
import com.nixmash.springdata.jpa.exceptions.DuplicatePostNameException;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.exceptions.TagNotFoundException;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.model.Tag;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.jpa.utils.PostUtils;
import com.nixmash.springdata.jsoup.dto.PagePreviewDTO;
import com.nixmash.springdata.jsoup.service.JsoupService;
import com.nixmash.springdata.mvc.components.WebUI;
import com.nixmash.springdata.mvc.containers.PostLink;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by daveburke on 5/27/16.
 */
@SuppressWarnings("Duplicates")
@Controller
@RequestMapping(value = "/posts")
public class PostsController {

    private static final Logger logger = LoggerFactory.getLogger(PostsController.class);

    // region constants

    protected static final String POSTS_LIST_VIEW = "posts/list";
    public static final String POSTS_ADD_VIEW = "posts/add";
    public static final String POSTS_PERMALINK_VIEW = "posts/post";
    public static final String POSTS_UPDATE_VIEW = "posts/update";
    public static final String POSTS_TITLES_VIEW = "posts/titles";
    public static final String FEEDBACK_POST_UPDATED = "feedback.post.updated";
    private static final String POSTS_TAGS_VIEW = "posts/tags";
    private static final String POSTS_TAGTITLES_VIEW = "posts/tagtitles";
    public static final String POSTS_LIKES_VIEW = "posts/likes";
    public static final String POSTS_AZ_VIEW = "posts/az";

    private static final String FEEDBACK_POST_LINK_ADDED = "feedback.post.link.added";
    private static final String FEEDBACK_POST_NOTE_ADDED = "feedback.post.note.added";
    private static final String FEEDBACK_LINK_DEMO_THANKS = "feedback.post.link.demo.added";
    public static final String FEEDBACK_POST_NOT_FOUND = "feedback.post.not.found";
    private static final String FEEDBACK_NOTE_DEMO_THANKS = "feedback.post.note.demo.added";

    private static final String ADD_POST_HEADER = "posts.add.note.page.header";
    private static final String ADD_LINK_HEADER = "posts.add.link.page.header";
    private static final String ADD_PHOTO_HEADER = "posts.add.photo.page.header";
    private static final String ADD_MULTIPHOTO_HEADER = "posts.add.multiphoto.page.header";


    public static final int POST_PAGING_SIZE = 10;
    public static final int TITLE_PAGING_SIZE = 10;

    // endregion

    // region beans

    private final WebUI webUI;
    private final JsoupService jsoupService;
    private final PostService postService;
    private final ApplicationSettings applicationSettings;

    // endregion

    // region constructor

    @Autowired
    public PostsController(WebUI webUI, JsoupService jsoupService, PostService postService,  ApplicationSettings applicationSettings) {
        this.webUI = webUI;
        this.jsoupService = jsoupService;
        this.postService = postService;
        this.applicationSettings = applicationSettings;
    }

    // endregion


    // region /posts get

    @RequestMapping(value = "", method = GET)
    public String home(Model model) {
        boolean showMore = postService.getAllPosts().size() > POST_PAGING_SIZE;
        model.addAttribute("showmore", showMore);
        return POSTS_LIST_VIEW;
    }

    @RequestMapping(value = "/feed", produces = "application/*")
    public String feed() {
        return "rssPostFeedView";
    }

    @RequestMapping(value = "/titles", method = GET)
    public String titles(Model model) {
        boolean showMore = postService.getAllPosts().size() > TITLE_PAGING_SIZE;
        model.addAttribute("showmore", showMore);
        return POSTS_TITLES_VIEW;
    }

    @RequestMapping(value = "/az", method = GET)
    public String postsAtoZ(Model model) {
        model.addAttribute("alphaPosts", postService.getAlphaPosts());
        model.addAttribute("alphaLinks", postService.getAlphaLInks());
        return POSTS_AZ_VIEW;
    }

    @RequestMapping(value = "/tag/{tagValue}", method = GET)
    public String tags(@PathVariable("tagValue") String tagValue, Model model)
                                        throws TagNotFoundException, UnsupportedEncodingException {
        Tag tag = postService.getTag(URLDecoder.decode(tagValue, "UTF-8"));
       boolean showMore = postService.getPostsByTagId(tag.getTagId()).size() > POST_PAGING_SIZE;
        model.addAttribute("tag", tag);
        model.addAttribute("showmore", showMore);
        return POSTS_TAGS_VIEW;
    }

    @RequestMapping(value = "/likes/{userId}", method = GET)
    public String tags(@PathVariable("userId") long userId, Model model) {
       boolean showMore = postService.getPostsByUserLikes(userId).size() > POST_PAGING_SIZE;
        model.addAttribute("showmore", showMore);
        return POSTS_LIKES_VIEW;
    }

    @RequestMapping(value = "/titles/tag/{tagValue}", method = GET)
    public String tagTitles(@PathVariable("tagValue") String tagValue, Model model)
            throws TagNotFoundException, UnsupportedEncodingException {
        Tag tag = postService.getTag(URLDecoder.decode(tagValue, "UTF-8"));
        boolean showMore = postService.getPostsByTagId(tag.getTagId()).size() > TITLE_PAGING_SIZE;
        model.addAttribute("tag", tag);
        model.addAttribute("showmore", showMore);
        return POSTS_TAGTITLES_VIEW;
    }

    // endregion

    // region /post get

    @RequestMapping(value = "/post/{postName}", method = GET)
    public String post(@PathVariable("postName") String postName, Model model, CurrentUser currentUser)
            throws PostNotFoundException {

        Post post = postService.getPost(postName);
        Date postCreated = Date.from(post.getPostDate().toInstant());
        post.setIsOwner(PostUtils.isPostOwner(currentUser, post.getUserId()));
        post.setPostContent(PostUtils.formatPostContent(post));
        model.addAttribute("post", post);
        model.addAttribute("postCreated", postCreated);
        model.addAttribute("shareSiteName",
                StringUtils.deleteWhitespace(applicationSettings.getSiteName()));
        model.addAttribute("shareUrl",
                String.format("%s/posts/post/%s", applicationSettings.getBaseUrl(), post.getPostName()));
        return POSTS_PERMALINK_VIEW;
    }

    // endregion

    // region /update {get / post}

    @PreAuthorize("@postService.canUpdatePost(authentication, #postId)")
    @RequestMapping(value = "/update/{postId}", method = GET)
    public String updatePost(@PathVariable("postId") Long postId,
                             Model model) throws PostNotFoundException {
        Post post = postService.getPostById(postId);

        model.addAttribute("postDTO", PostDTO.getUpdateFields(post.getPostId(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getDisplayType())
                .tags(PostUtils.tagsToTagDTOs(post.getTags()))
                .build());
        return POSTS_UPDATE_VIEW;
    }

    @RequestMapping(value = "/update", method = POST)
    public String updatePost(@Valid PostDTO postDTO, BindingResult result, Model model,
                             RedirectAttributes attributes) throws PostNotFoundException {
            if (result.hasErrors()) {
                model.addAttribute("postDTO", postDTO);
                return POSTS_UPDATE_VIEW;
            } else {
                postDTO.setPostContent(cleanContentTailHtml(postDTO.getPostContent()));
                Post post = postService.update(postDTO);
                webUI.addFeedbackMessage(attributes, FEEDBACK_POST_UPDATED);
                return "redirect:/posts/post/" + post.getPostName();
            }
    }

    // endregion

    // region /add {get} methods

    @RequestMapping(value = "/add", method = GET, params = {"formtype"})
    public String displayAddPostForm(@RequestParam(value = "formtype") String formType,
                                     PostLink postLink, BindingResult result, Model model, HttpServletRequest request) {
        PostType postType = PostType.valueOf(formType.toUpperCase());
        String postFormType;

        if (postType.equals(PostType.NOTE)) {
            postFormType = "note";
            model.addAttribute("postDTO", new PostDTO());
            model.addAttribute("postheader", webUI.getMessage(ADD_POST_HEADER));
        } else {
            model.addAttribute("postheader", webUI.getMessage(ADD_LINK_HEADER));
            if (StringUtils.isEmpty(postLink.getLink())) {
                result.rejectValue("link", "post.link.is.empty");
                return POSTS_ADD_VIEW;
            } else {
                PagePreviewDTO pagePreview = jsoupService.getPagePreview(postLink.getLink());
                if (pagePreview == null) {
                    result.rejectValue("link", "post.link.page.not.found");
                    return POSTS_ADD_VIEW;
                } else {
                    postFormType = "link";
                    WebUtils.setSessionAttribute(request, "pagePreview", pagePreview);
                    model.addAttribute("pagePreview", pagePreview);
                    model.addAttribute("postDTO",
                            postDtoFromPagePreview(pagePreview, postLink.getLink()));
                }
            }
        }

        model.addAttribute("postFormType", postFormType);
        return POSTS_ADD_VIEW;
    }

    @RequestMapping(value = "/add", method = GET)
    public String addPost(Model model) {
        model.addAttribute("postLink", new PostLink());
        return POSTS_ADD_VIEW;
    }

    // endregion

    // region /add {post} methods

    @RequestMapping(value = "/add", method = POST, params = {"note"})
    public String createNotePost(@Valid PostDTO postDTO, BindingResult result,
                             CurrentUser currentUser, RedirectAttributes attributes, Model model,
                             HttpServletRequest request) throws DuplicatePostNameException {

        model.addAttribute("postheader", webUI.getMessage(ADD_POST_HEADER));
        model.addAttribute("postFormType", "note");

        if (!isDuplicatePost(postDTO)) {
            if (result.hasErrors()) {
                model.addAttribute("postDTO", postDTO);
                return POSTS_ADD_VIEW;
            } else {
                if (canPost(currentUser)) {

                    postDTO.setPostName(PostUtils.createSlug(postDTO.getPostTitle()));
                    postDTO.setUserId(currentUser.getId());
                    postDTO.setPostContent(cleanContentTailHtml(postDTO.getPostContent()));

                    request.setAttribute("postTitle", postDTO.getPostTitle());
                    postService.add(postDTO);

                    webUI.addFeedbackMessage(attributes, FEEDBACK_POST_NOTE_ADDED);
                    return "redirect:/posts";
                } else {
                    webUI.addFeedbackMessage(attributes, FEEDBACK_NOTE_DEMO_THANKS);
                    return "redirect:/posts/add";
                }
            }
        } else {
            result.reject("global.error.post.name.exists", new Object[]{postDTO.getPostTitle()}, "post name exists");
            return POSTS_ADD_VIEW;
        }
    }

    @RequestMapping(value = "/add", method = POST, params = {"link"})
    public String createLinkPost(@Valid PostDTO postDTO, BindingResult result,
                             CurrentUser currentUser, RedirectAttributes attributes, Model model,
                             HttpServletRequest request) throws DuplicatePostNameException {
        PagePreviewDTO pagePreview =
                (PagePreviewDTO) WebUtils.getSessionAttribute(request, "pagePreview");

        model.addAttribute("postheader", webUI.getMessage(ADD_LINK_HEADER));
        model.addAttribute("postFormType", "link");

        if (!isDuplicatePost(postDTO)) {
            if (result.hasErrors()) {
                model.addAttribute("pagePreview", pagePreview);
                if (result.hasFieldErrors("postTitle")) {
                    postDTO.setPostTitle(pagePreview.getTitle());
                }
                model.addAttribute("postDTO", postDTO);
                return POSTS_ADD_VIEW;
            } else {
                if (canPost(currentUser)) {

                    if (postDTO.getHasImages()) {
                        if (postDTO.getDisplayType() != PostDisplayType.LINK) {
                            postDTO.setPostImage(
                                    pagePreview.getImages().get(postDTO.getImageIndex()).src);
                        } else
                            postDTO.setPostImage(null);
                    }

                    postDTO.setPostSource(PostUtils.createPostSource(postDTO.getPostLink()));
                    postDTO.setPostName(PostUtils.createSlug(postDTO.getPostTitle()));
                    postDTO.setUserId(currentUser.getId());
                    postDTO.setPostContent(cleanContentTailHtml(postDTO.getPostContent()));

                    request.setAttribute("postTitle", postDTO.getPostTitle());
                    postService.add(postDTO);

                    webUI.addFeedbackMessage(attributes, FEEDBACK_POST_LINK_ADDED);
                    return "redirect:/posts";
                } else {
                    webUI.addFeedbackMessage(attributes, FEEDBACK_LINK_DEMO_THANKS);
                    return "redirect:/posts/add";
                }
            }
        } else {
            result.reject("global.error.post.name.exists", new Object[]{postDTO.getPostTitle()}, "post name exists");
            model.addAttribute("pagePreview", pagePreview);
            return POSTS_ADD_VIEW;
        }
    }

    private Boolean canPost(CurrentUser currentUser) {
        Boolean canPost = false;
        if (currentUser != null) {
            if (currentUser.getUser().hasAuthority(Role.ROLE_POSTS))
                canPost = true;
        }
        return canPost;
    }
    // endregion

    // region postDTO Utilities

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


    private Boolean isDuplicatePost(PostDTO postDTO) {
        Boolean isDuplicate = false;
        if (StringUtils.isNotEmpty(postDTO.getPostTitle())) {
            String slug = PostUtils.createSlug(postDTO.getPostTitle());
            Post found = null;
            try {
                found = postService.getPost(slug);
            } catch (PostNotFoundException e) {
                logger.error("No post found for post name: " + slug);
            }
            if (found != null) {
                isDuplicate = true;
            }
        }
        return isDuplicate;
    }

    // endregion

}