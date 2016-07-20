package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.dto.TagDTO;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.jpa.utils.Pair;
import com.nixmash.springdata.jpa.utils.PostUtils;
import com.nixmash.springdata.mail.service.TemplateService;
import com.nixmash.springdata.mvc.annotations.JsonRequestMapping;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;


@RestController
@JsonRequestMapping(value = "/json/posts")
public class PostsRestController {

    private static final Logger logger = LoggerFactory.getLogger(PostsRestController.class);
    private static final String TITLE_TEMPLATE = "title";

    private PostService postService;
    private TemplateService templateService;
    private ApplicationSettings applicationSettings;

    private int minTagCount = 0;
    private int maxTagCount = 0;

    @Autowired
    public PostsRestController(PostService postService, TemplateService templateService, ApplicationSettings applicationSettings) {
        this.postService = postService;
        this.templateService = templateService;
        this.applicationSettings = applicationSettings;
    }

    // region get  Post Titles

    @RequestMapping(value = "/titles/{pageNumber}", produces = "text/html;charset=UTF-8")
    public String getPostTitles(@PathVariable Integer pageNumber, HttpServletRequest request, CurrentUser currentUser) {
        Slice<Post> posts = postService.getPosts(pageNumber, 10);
        String result = StringUtils.EMPTY;
        for (Post post : posts) {
            post.setIsOwner(PostUtils.isPostOwner(currentUser, post.getUserId()));
            result += templateService.createPostHtml(post, TITLE_TEMPLATE);
        }
        WebUtils.setSessionAttribute(request, "posttitles", posts);
        return result;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/titles/more")
    public String getTitleHasNext(HttpServletRequest request) {
        Slice<Post> posts = (Slice<Post>) WebUtils.getSessionAttribute(request, "posttitles");
        if (posts != null)
            return Boolean.toString(posts.hasNext());
        else
            return "true";
    }

    // endregion

    // region get Posts by Tag

    @RequestMapping(value = "/titles/tag/{tagid}/page/{pageNumber}",
            produces = "text/html;charset=UTF-8")
    public String getPostTitlesByTagId(@PathVariable long tagid,
                                       @PathVariable int pageNumber,
                                       HttpServletRequest request,
                                       CurrentUser currentUser) {
        Slice<Post> posts = postService.getPostsByTagId(tagid, pageNumber, 10);
        String result = StringUtils.EMPTY;
        for (Post post : posts) {
            post.setIsOwner(PostUtils.isPostOwner(currentUser, post.getUserId()));
            result += templateService.createPostHtml(post, TITLE_TEMPLATE);
        }
        WebUtils.setSessionAttribute(request, "taggedposttitles", posts);
        return result;
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/titles/tag/{tagid}/more")
    public String getTagTitlesHasNext(@PathVariable int tagid, HttpServletRequest request) {
        Slice<Post> posts = (Slice<Post>) WebUtils.getSessionAttribute(request, "taggedposttitles");
        if (posts != null)
            return Boolean.toString(posts.hasNext());
        else
            return "true";
    }

    // endregion

    // region get all Posts

    @RequestMapping(value = "/page/{pageNumber}", produces = "text/html;charset=UTF-8")
    public String getPosts(@PathVariable Integer pageNumber, HttpServletRequest request, CurrentUser currentUser) {
        Slice<Post> posts = postService.getPosts(pageNumber, 10);
        String result = StringUtils.EMPTY;
        for (Post post : posts) {
            post.setIsOwner(PostUtils.isPostOwner(currentUser, post.getUserId()));
            result += templateService.createPostHtml(post);
        }
        WebUtils.setSessionAttribute(request, "posts", posts);
        return result;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/more")
    public String getHasNext(HttpServletRequest request) {
        Slice<Post> posts = (Slice<Post>) WebUtils.getSessionAttribute(request, "posts");
        if (posts != null)
            return Boolean.toString(posts.hasNext());
        else
            return "true";
    }

    // endregion

    // region get Posts by Tag

    @RequestMapping(value = "/tag/{tagid}/page/{pageNumber}",
            produces = "text/html;charset=UTF-8")
    public String getPostsByTagId(@PathVariable long tagid,
                                  @PathVariable int pageNumber,
                                  HttpServletRequest request,
                                  CurrentUser currentUser) {
        Slice<Post> posts = postService.getPostsByTagId(tagid, pageNumber, 10);
        String result = StringUtils.EMPTY;
        for (Post post : posts) {
            post.setIsOwner(PostUtils.isPostOwner(currentUser, post.getUserId()));
            result += templateService.createPostHtml(post);
        }
        WebUtils.setSessionAttribute(request, "taggedposts", posts);
        return result;
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/tag/{tagid}/more")
    public String getHasNext(@PathVariable int tagid, HttpServletRequest request) {
        Slice<Post> posts = (Slice<Post>) WebUtils.getSessionAttribute(request, "taggedposts");
        if (posts != null)
            return Boolean.toString(posts.hasNext());
        else
            return "true";
    }

    // endregion

    // region Likes

    @RequestMapping(value = "/post/like/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public int likePost(@PathVariable("postId") int postId) {
        return -1;
    }
    // endregion

    // region get Tags

    @RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<TagDTO> getAllTagDTOs() {
        return postService.getTagDTOs();
    }

    @RequestMapping(value = "/tagvalues", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getTagValues() {
        return postService.getTagValues();
    }


    @RequestMapping(value = "/tagcloud", produces = "text/html;charset=UTF-8")
    public String getTagCloud() {

        List<TagDTO> tags = postService.getTagCloud();
        maxTagCount = tags.stream().mapToInt(TagDTO::getTagCount).max().orElse(0);
        minTagCount = tags.stream().mapToInt(TagDTO::getTagCount).min().orElse(0);

        StringBuilder tagHtml = new StringBuilder();
        tagHtml.append("<ul class='taglist'>");
        for (TagDTO tag : tags) {
            tagHtml.append(tagHtml(tag));
        }
        tagHtml.append("</ul>");
        return tagHtml.toString();
    }

    private String tagHtml(TagDTO tag) {
        String tagPattern = "<li><a href='%s/posts/tag/%s' class='%s'>%s</a></li>";
        String cssClass = getCssTag(tag.getTagCount());
        String tagLowerCase = tag.getTagValue().toLowerCase();

        return String.format(tagPattern,
                applicationSettings.getBaseUrl(), tagLowerCase, cssClass, tag.getTagValue());
    }

    // endregion

    // region Tag Cloud

    private String getCssTag(int tagCount) {

        String cssClass = "smallTag";
        int diff = maxTagCount - minTagCount;
        int distribution = diff / 5;

        if (tagCount == maxTagCount)
            cssClass = "maxTag";
        else if (tagCount == minTagCount)
            cssClass = "minTag";
        else if (tagCount > (minTagCount + (distribution * 1.75)))
            cssClass = "largeTag";
        else if (tagCount > (minTagCount + distribution))
            cssClass = "mediumTag";

        return cssClass;
    }

    // endregion

    // region Key-Value Json

    //
    // --- demo for NixMash Post "Variations on JSON Key-Value Pairs in Spring MVC"  http://goo.gl/0hhnZg

    private String key = "key";
    private String value = "Json Key-Value Demo";

    /*
    *           Returns:  {  "key" : "Json Key-Value Demo"  }
     */
    @RequestMapping(value = "/map", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> returnMap() {
        Map<String, String> keyvalues = new HashMap<>();
        keyvalues.put(key, value);
        return keyvalues;
    }

    /*
    *           Returns:  {  "key" : "Json Key-Value Demo"  }
     */
    @RequestMapping(value = "/simpleentry")
    public SimpleEntry<String, String> returnSimpleEntry() {
        return new SimpleEntry<>(key, value);
    }

    /*
    *           Returns:  {  "key" : "Json Key-Value Demo"  }
     */
    @RequestMapping(value = "/singleton")
    public Map<String, String> returnSingletonMapFromCollection() {
        return Collections.singletonMap(key, value);
    }

    /*
    *           Returns:
    *           {
    *                    "key" : "key",
    *                     "value" : "Json Key-Value Demo"
    *           }
     */
    @RequestMapping(value = "/pair")
    public Pair<String, String> returnPair() {
        return new Pair<>(key, value);
    }

    // endregion

}
