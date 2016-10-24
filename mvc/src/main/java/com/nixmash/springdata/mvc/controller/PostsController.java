package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.dto.PostQueryDTO;
import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.exceptions.TagNotFoundException;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.model.Tag;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.jpa.utils.PostUtils;
import com.nixmash.springdata.jsoup.service.JsoupService;
import com.nixmash.springdata.mail.service.FmService;
import com.nixmash.springdata.mvc.components.WebUI;
import com.nixmash.springdata.solr.model.PostDoc;
import com.nixmash.springdata.solr.service.PostDocService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
    public static final String POSTS_SEARCH_VIEW = "posts/search";
    public static final String POSTS_QUICKSEARCH_VIEW = "posts/quicksearch";
    public static final String POSTS_LINKS_VIEW = "posts/links";

    private static final String FEEDBACK_POST_LINK_ADDED = "feedback.post.link.added";
    private static final String FEEDBACK_POST_NOTE_ADDED = "feedback.post.note.added";
    private static final String FEEDBACK_LINK_DEMO_THANKS = "feedback.post.link.demo.added";

    public static final String FEEDBACK_POST_NOT_FOUND = "feedback.post.not.found";
    private static final String FEEDBACK_NOTE_DEMO_THANKS = "feedback.post.note.demo.added";
    private static final String ADD_POST_HEADER = "posts.add.note.page.header";
    private static final String ADD_LINK_HEADER = "posts.add.link.page.header";

    private static final String ADD_PHOTO_HEADER = "posts.add.photo.page.header";
    private static final String ADD_MULTIPHOTO_HEADER = "posts.add.multiphoto.page.header";


    public static final String POST_PUBLISH = "publish";
    public static final String POST_DRAFT = "draft";
    public static final int POST_PAGING_SIZE = 10;

    public static final int TITLE_PAGING_SIZE = 10;
    private static final String SESSION_ATTRIBUTE_NEWPOST = "activepostdto";
    public static final String SESSION_QUICKSEARCH_QUERY = "quicksearch";
    public static final String SESSION_POSTQUERYDTO = "postquerydto";

    // endregion

    // region beans

    private final WebUI webUI;
    private final JsoupService jsoupService;
    private final PostService postService;
    private final ApplicationSettings applicationSettings;
    private final FmService fmService;
    private final PostDocService postDocService;

    // endregion

    // region constructor

    @Autowired
    public PostsController(WebUI webUI, JsoupService jsoupService, PostService postService, ApplicationSettings applicationSettings, FmService fmService, PostDocService postDocService) {
        this.webUI = webUI;
        this.jsoupService = jsoupService;
        this.postService = postService;
        this.applicationSettings = applicationSettings;
        this.fmService = fmService;
        this.postDocService = postDocService;
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

    // region /posts get

    @RequestMapping(value = "", method = GET)
    public String home(Model model) {
        boolean showMore = postService.getAllPublishedPosts().size() > POST_PAGING_SIZE;
        model.addAttribute("showmore", showMore);
        return POSTS_LIST_VIEW;
    }

    @RequestMapping(value = "/search", method = GET)
    public String searchPage(Model model, HttpServletRequest request) {
        model.addAttribute("postQueryDTO", new PostQueryDTO());
        model.addAttribute("isSearchResult", false);
        WebUtils.setSessionAttribute(request, SESSION_POSTQUERYDTO, null);
        return POSTS_SEARCH_VIEW;
    }

    @RequestMapping(value = "/search",  params = {"query"}, method = GET)
    public String searchPageResults(@Valid PostQueryDTO postQueryDTO,
                                    BindingResult result, Model model, HttpServletRequest request) {
        model.addAttribute("postQuery", postQueryDTO);
        if (result.hasErrors()) {
            return POSTS_SEARCH_VIEW;
        } else {
            WebUtils.setSessionAttribute(request, SESSION_POSTQUERYDTO, postQueryDTO);
            model.addAttribute("isSearchResult", true);
            return POSTS_SEARCH_VIEW;
        }
    }

    @RequestMapping(value = "", params = {"search"}, method = GET)
    public String quicksearch(Model model, String search, HttpServletRequest request) {
        List<PostDoc> postDocs = postDocService.doQuickSearch(search);

        boolean showMore = postDocs.size() > POST_PAGING_SIZE;
        boolean hasQuickSearchResults = postDocs.size() > 0;

        WebUtils.setSessionAttribute(request, SESSION_QUICKSEARCH_QUERY, search);
        model.addAttribute("showmore", showMore);
        model.addAttribute("query", search);
        model.addAttribute("hasResults", hasQuickSearchResults);
        return POSTS_QUICKSEARCH_VIEW;
    }


    @RequestMapping(value = "/feed", produces = "application/*")
    public String feed() {
        return "rssPostFeedView";
    }

    @RequestMapping(value = "/titles", method = GET)
    public String titles(Model model) {
        boolean showMore = postService.getAllPublishedPosts().size() > TITLE_PAGING_SIZE;
        model.addAttribute("showmore", showMore);
        return POSTS_TITLES_VIEW;
    }

    @RequestMapping(value = "/links", method = GET)
    public String justlinks(Model model) {
        boolean showMore = postService.getAllPublishedPostsByPostType(PostType.LINK).size() > POST_PAGING_SIZE;
        model.addAttribute("showmore", showMore);
        return POSTS_LINKS_VIEW;
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
    public String userLikes(@PathVariable("userId") long userId, Model model) {
        boolean showMore = false;
        if (postService.getPostsByUserLikes(userId) != null)
            showMore = postService.getPostsByUserLikes(userId).size() > POST_PAGING_SIZE;
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

}