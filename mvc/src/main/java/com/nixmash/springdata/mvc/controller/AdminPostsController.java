package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.dto.TagDTO;
import com.nixmash.springdata.jpa.enums.PostDisplayType;
import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.exceptions.DuplicatePostNameException;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.model.Tag;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.jpa.utils.PostUtils;
import com.nixmash.springdata.jsoup.dto.PagePreviewDTO;
import com.nixmash.springdata.jsoup.service.JsoupService;
import com.nixmash.springdata.mail.service.FmService;
import com.nixmash.springdata.mvc.components.WebUI;
import com.nixmash.springdata.mvc.containers.PostLink;
import com.nixmash.springdata.solr.model.PostDoc;
import com.nixmash.springdata.solr.service.PostDocService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by daveburke on 8/29/16.
 */
@SuppressWarnings("Duplicates")
@Controller
@RequestMapping(value = "/admin/posts")
public class AdminPostsController {

    // region static view properties

    public static final String ADMIN_POSTS_LIST_VIEW = "admin/posts/list";
    public static final String ADMIN_POST_ADD_VIEW = "admin/posts/addpost";
    public static final String ADMIN_LINK_ADD_VIEW = "admin/posts/addlink";
    public static final String ADMIN_TAGS_VIEW = "admin/posts/tags";
    public static final String ADMIN_POSTLINK_UPDATE_VIEW = "admin/posts/update";
    public static final String ADMIN_POSTS_REINDEX_VIEW = "admin/posts/reindex";

    // endregion

    // region static message.properites

    private static final String MESSAGE_ADMIN_UPDATE_POSTLINK_TITLE = "admin.update.postlink.title";
    private static final String MESSAGE_ADMIN_SOLR_REINDEX_COMPLETE = "admin.solr.posts.reindexed";
    private static final String MESSAGE_ADMIN_UPDATE_POSTLINK_HEADING = "admin.update.postlink.heading";

    private static final String ADD_POST_HEADER = "posts.add.note.page.header";
    private static final String ADD_LINK_HEADER = "posts.add.link.page.header";

    private static final String FEEDBACK_POST_LINK_ADDED = "feedback.post.link.added";
    private static final String FEEDBACK_POST_POST_ADDED = "feedback.post.post.added";
    public static final String FEEDBACK_POST_UPDATED = "feedback.post.updated";
    private static final String FEEDBACK_MESSAGE_TAG_ADDED = "feedback.message.tag.added";
    private static final String FEEDBACK_MESSAGE_TAG_ERROR = "feedback.message.tag.error";
    private static final String FEEDBACK_MESSAGE_TAG_UPDATED = "feedback.message.tag.updated";
    private static final String FEEDBACK_MESSAGE_TAG_DELETED = "feedback.message.tag.deleted";

    // endregion

    // region class variables

    private static final String SESSION_ATTRIBUTE_NEWPOST = "activepostdto";

    public static final String POST_PUBLISH = "publish";
    public static final String POST_DRAFT = "draft";

    // endregion

    // region beans

    private final PostService postService;
    private final PostDocService postDocService;
    private final WebUI webUI;
    private final FmService fmService;
    private final JsoupService jsoupService;

    // endregion

    private static final Logger logger = LoggerFactory.getLogger(AdminPostsController.class);

    @Autowired
    public AdminPostsController(PostService postService, PostDocService postDocService, WebUI webUI, FmService fmService, JsoupService jsoupService) {
        this.postService = postService;
        this.postDocService = postDocService;
        this.webUI = webUI;
        this.fmService = fmService;
        this.jsoupService = jsoupService;
    }

    //region Posts List

    @RequestMapping(value = "", method = GET)
    public ModelAndView postsListPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("posts", postService.getAllPosts());
        mav.setViewName(ADMIN_POSTS_LIST_VIEW);
        return mav;
    }

    //endregion

    // region Solr

    @RequestMapping(value = "/solr/reindex", method = GET)
    public ModelAndView postsSolrReindexPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(ADMIN_POSTS_REINDEX_VIEW);
        return mav;
    }

    @RequestMapping(value = "/solr/reindex", params = "reindex",method = GET)
    public ModelAndView reindexSolrPosts() {
        ModelAndView mav = new ModelAndView();
        List<Post> posts = postService.getAllPublishedPosts();
        int postDocCount = posts.size();

        long lStartTime = new Date().getTime();
        postDocService.reindexPosts(posts);
        long lEndTime = new Date().getTime();
        long duration = lEndTime - lStartTime;

        String totalTime = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );


        String reindexMessage = webUI.getMessage(MESSAGE_ADMIN_SOLR_REINDEX_COMPLETE, postDocCount, totalTime);

        mav.addObject("reindexMessage", reindexMessage);
        mav.addObject("hasPostDocCount", true);
        mav.setViewName(ADMIN_POSTS_REINDEX_VIEW);
        return mav;
    }


    // endregion

    //region Add Posts GET

    @RequestMapping(value = "/add/{type}", method = GET)
    public String addPostLink(@PathVariable("type") String type, Model model, HttpServletRequest request) {
        PostType postType = PostType.valueOf(type.toUpperCase());
        model.addAttribute("postDTO", new PostDTO());
        model.addAttribute("canPreview", false);
        if (postType == PostType.POST) {
            WebUtils.setSessionAttribute(request, SESSION_ATTRIBUTE_NEWPOST, null);
            model.addAttribute("hasPost", true);
            model.addAttribute("postheader", webUI.getMessage(ADD_POST_HEADER));
            return ADMIN_POST_ADD_VIEW;
        } else {
            model.addAttribute("postLink", new PostLink());
            model.addAttribute("postheader", webUI.getMessage(ADD_LINK_HEADER));
            return ADMIN_LINK_ADD_VIEW;
        }
    }

    @RequestMapping(value = "/add/link", params = {"isLink"}, method = GET)
    public String addLink(@RequestParam(value = "isLink") Boolean isLink,
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
                model.addAttribute("hasCarousel", true);
                WebUtils.setSessionAttribute(request, "pagePreview", pagePreview);
                model.addAttribute("pagePreview", pagePreview);
                model.addAttribute("postDTO",
                        postDtoFromPagePreview(pagePreview, postLink.getLink()));
            }
        }
        return ADMIN_LINK_ADD_VIEW;
    }

    //endregion

    //region Add Posts POST

    @RequestMapping(value = "/add/link", method = POST)
    public String createLinkPost(@Valid PostDTO postDTO, BindingResult result,
                                 CurrentUser currentUser, RedirectAttributes attributes, Model model,
                                 HttpServletRequest request) throws DuplicatePostNameException {
        PagePreviewDTO pagePreview =
                (PagePreviewDTO) WebUtils.getSessionAttribute(request, "pagePreview");

        model.addAttribute("postheader", webUI.getMessage(ADD_LINK_HEADER));
        model.addAttribute("postFormType", "link");

        if (!isDuplicatePost(postDTO, null)) {
            if (result.hasErrors()) {
                model.addAttribute("hasLink", true);
                model.addAttribute("hasCarousel", true);
                model.addAttribute("pagePreview", pagePreview);
                if (result.hasFieldErrors("postTitle")) {
                    postDTO.setPostTitle(pagePreview.getTitle());
                }
                model.addAttribute("postDTO", postDTO);
                return ADMIN_LINK_ADD_VIEW;
            } else {

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
                Post post = postService.add(postDTO);

                // All links are saved as PUBLISHED so no _isPublished_ status check
                postDocService.addToIndex(post);

                webUI.addFeedbackMessage(attributes, FEEDBACK_POST_LINK_ADDED);
                return "redirect:/admin/posts";
            }
        } else {
            result.reject("global.error.post.name.exists", new Object[]{postDTO.getPostTitle()}, "post name exists");
            model.addAttribute("hasLink", true);
            model.addAttribute("hasCarousel", true);
            model.addAttribute("pagePreview", pagePreview);
            return ADMIN_LINK_ADD_VIEW;
        }
    }


    @RequestMapping(value = "/add/post", method = POST)
    public String createNotePost(@Valid PostDTO postDTO, BindingResult result,
                                 CurrentUser currentUser, RedirectAttributes attributes, Model model,
                                 HttpServletRequest request) throws DuplicatePostNameException, PostNotFoundException {

        String saveAction = request.getParameter("post");

        model.addAttribute("postheader", webUI.getMessage(ADD_POST_HEADER));
        model.addAttribute("hasPost", true);
        model.addAttribute("canPreview", false);

        Post sessionPost = null;
        Object obj = WebUtils.getSessionAttribute(request, SESSION_ATTRIBUTE_NEWPOST);
        if (obj != null) {
            sessionPost = (Post) WebUtils.getSessionAttribute(request, SESSION_ATTRIBUTE_NEWPOST);
        }
        if (!isDuplicatePost(postDTO, sessionPost)) {
            if (result.hasErrors()) {
                model.addAttribute("postDTO", postDTO);
                return ADMIN_POST_ADD_VIEW;
            } else {
                postDTO.setDisplayType(postDTO.getDisplayType());
                postDTO.setPostName(PostUtils.createSlug(postDTO.getPostTitle()));
                postDTO.setUserId(currentUser.getId());
                postDTO.setPostContent(cleanContentTailHtml(postDTO.getPostContent()));
                postDTO.setIsPublished(saveAction.equals(POST_PUBLISH));

                request.setAttribute("postTitle", postDTO.getPostTitle());
                Post saved;

                if (sessionPost == null)
                    saved = postService.add(postDTO);
                else {
                    postDTO.setPostId(sessionPost.getPostId());
                    saved = postService.update(postDTO);
                }

                postDTO.setPostId(saved.getPostId());
                WebUtils.setSessionAttribute(request, SESSION_ATTRIBUTE_NEWPOST, saved);

                if (saveAction.equals(POST_PUBLISH)) {

                    if (saved.getIsPublished())
                        postDocService.addToIndex(saved);

                    webUI.addFeedbackMessage(attributes, FEEDBACK_POST_POST_ADDED);
                    return "redirect:/admin/posts";
                } else {
                    model.addAttribute("fileuploading", fmService.getFileUploadingScript());
                    model.addAttribute("fileuploaded", fmService.getFileUploadedScript());
                    model.addAttribute("postDTO", getUpdatedPostDTO(saved));
                    model.addAttribute("hasImageUploads", true);
                    model.addAttribute("canPreview", true);
                    model.addAttribute("postName", saved.getPostName());
                    return ADMIN_POST_ADD_VIEW;
                }
            }
        } else {
            model.addAttribute("hasImageUploads", true);
            result.reject("global.error.post.name.exists", new Object[]{postDTO.getPostTitle()}, "post name exists");
            return ADMIN_POST_ADD_VIEW;
        }
    }

    //endregion

    //region Update Posts GET POST

    @RequestMapping(value = "/update/{postId}", method = GET)
    public String updatePost(@PathVariable("postId") Long postId,
                             Model model, HttpServletRequest request) throws PostNotFoundException {
        Post post = postService.getPostById(postId);
        String postType = StringUtils.capitalize(post.getPostType().name().toLowerCase());
        String pageTitle = webUI.getMessage(MESSAGE_ADMIN_UPDATE_POSTLINK_TITLE, postType);
        String pageHeading = webUI.getMessage(MESSAGE_ADMIN_UPDATE_POSTLINK_HEADING, postType);

        PostDTO postDTO = getUpdatedPostDTO(post);
        if (post.getPostType() == PostType.LINK) {
            postDTO.setHasImages(post.getPostImage() != null);
            postDTO.setPostImage(post.getPostImage());
            if (postDTO.getHasImages()) {
                model.addAttribute("hasLinkImage", true);
            }
        }
        model.addAttribute("postName", post.getPostName());
        model.addAttribute("postDTO", postDTO);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageHeading", pageHeading);

        model.addAllAttributes(getPostLinkAttributes(request, post.getPostType()));

        return ADMIN_POSTLINK_UPDATE_VIEW;
    }

    @RequestMapping(value = "/update", method = POST)
    public String updatePost(@Valid PostDTO postDTO, BindingResult result, Model model,
                             RedirectAttributes attributes, HttpServletRequest request) throws PostNotFoundException {
        if (result.hasErrors()) {
            model.addAttribute("postDTO", postDTO);
            model.addAllAttributes(getPostLinkAttributes(request, postDTO.getPostType()));
            return ADMIN_POSTLINK_UPDATE_VIEW;
        } else {
            postDTO.setPostContent(cleanContentTailHtml(postDTO.getPostContent()));
            Post post = postService.update(postDTO);

            PostDoc postDoc = postDocService.getPostDocByPostId(post.getPostId());
            boolean postIsIndexed = postDoc != null;
            if (post.getIsPublished()) {
                if (postIsIndexed)
                    postDocService.updatePostDocument(post);
                else
                    postDocService.addToIndex(post);
            } else {
                // remove postDocument from Solr Index if previously marked "Published", now marked "Draft"
                if (postIsIndexed)
                    postDocService.removeFromIndex(postDoc);
            }



            webUI.addFeedbackMessage(attributes, FEEDBACK_POST_UPDATED);
            return "redirect:/admin/posts";
        }
    }

    @RequestMapping(value = "/archive", method = POST)
    @ResponseBody
    public String archivePost(@Valid @RequestBody PostDTO postDTO,
                              BindingResult result) throws PostNotFoundException {
        if (result.hasErrors()) {
            return "ERROR";
        } else {
            postDTO.setPostContent(cleanContentTailHtml(postDTO.getPostContent()));
            postService.update(postDTO);
            return "SUCCESS";
        }
    }

    private Map<String, Object> getPostLinkAttributes(HttpServletRequest request, PostType postType) {

        Map<String, Object> attributes = new HashMap<>();
        if (postType == PostType.POST) {
            attributes.put("hasPost", true);
            attributes.put("hasImageUploads", true);
            attributes.put("fileuploading", fmService.getFileUploadingScript());
            attributes.put("fileuploaded", fmService.getFileUploadedScript());
        } else
            attributes.put("hasLink", true);
        return attributes;
    }
    //endregion

    // region Tags

    @RequestMapping(value = "/tags", method = GET)
    public ModelAndView roleList(Model model) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("tags", postService.getTagCloud(Integer.MAX_VALUE));
        mav.addObject("newTag", new Tag());
        mav.setViewName(ADMIN_TAGS_VIEW);
        return mav;
    }

    @RequestMapping(value = "/tags/new", method = RequestMethod.POST)
    public String addUser(@Valid TagDTO tagDTO,
                          BindingResult result,
                          SessionStatus status,
                          RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return ADMIN_TAGS_VIEW;
        } else {

            Tag tag = postService.createTag(tagDTO);
            logger.info("Tag Added: {}", tag.getTagValue());
            status.setComplete();

            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_TAG_ADDED, tag.getTagValue());
            return "redirect:/admin/posts/tags";
        }
    }


    @RequestMapping(value = "/tags/update", method = RequestMethod.POST)
    public String updateRole(@Valid @ModelAttribute(value = "tag") TagDTO tagDTO, BindingResult result,
                             RedirectAttributes attributes) {
        if (result.hasErrors()) {
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_TAG_ERROR);
            return "redirect:/admin/posts/tags";
        } else {
            postService.updateTag(tagDTO);
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_TAG_UPDATED, tagDTO.getTagValue());
            return "redirect:/admin/posts/tags";
        }
    }

    @RequestMapping(value = "/tags/update", params = {"deleteTag"}, method = RequestMethod.POST)
    public String deleteRole(@Valid @ModelAttribute(value = "tag") TagDTO tagDTO, BindingResult result,
                             RedirectAttributes attributes) {
        if (result.hasErrors()) {
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_TAG_ERROR);
            return "redirect:/admin/posts/tags";
        } else {
            List<Post> posts = postService.getPostsByTagId(tagDTO.getTagId());
            postService.deleteTag(tagDTO, posts);
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_TAG_DELETED, tagDTO.getTagValue());
        }

        return "redirect:/admin/posts/tags";
    }


    // endregion

    // region postDTO Utilities

    private PostDTO getUpdatedPostDTO(Post post) {
        return PostDTO.getUpdateFields(post.getPostId(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getIsPublished(),
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

        if (postSource != null) {
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
            } catch (PostNotFoundException e) {
                // can be null for this check of a pre-existing post
            }
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
